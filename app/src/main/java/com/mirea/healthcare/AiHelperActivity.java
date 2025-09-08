package com.mirea.healthcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AiHelperActivity extends BaseActivity implements MessageAdapter.OnMessageLongClickListener {

    private AppDatabase db;
    private MessageDao messageDao;
    private int userId;
    private RecyclerView chatRecyclerView;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();
    private EditText userInput;
    private ImageView sendButton, voiceInputButton;
    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ai_helper);

        setTitle(R.string.ai_assistant_title);

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) { // Проверяем, что ID не -1 (неавторизованный пользователь)
            Toast.makeText(this, "User not identified!", Toast.LENGTH_SHORT).show();
            finish();
            return; // Добавьте return, чтобы код дальше не выполнялся
        }

        db = AppDatabase.getDatabase(this);
        messageDao = db.messageDao();

        new Thread(() -> {
            List<Message> savedMessages = messageDao.getMessagesByUser(userId);
            runOnUiThread(() -> {
                messages.addAll(savedMessages);
                adapter.notifyDataSetChanged();
                scrollToBottom();
            });
        }).start();

        // Инициализация элементов
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        userInput = findViewById(R.id.user_input);
        sendButton = findViewById(R.id.send_button);
        voiceInputButton = findViewById(R.id.voice_input_button);

        sendButton.setOnClickListener(v -> {
            v.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100)
                    .withEndAction(() -> {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100);
                        sendMessage();
                    });
        });

        // Настройка RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);

        adapter = new MessageAdapter(messages, this);
        chatRecyclerView.setAdapter(adapter);

        userInput.setHint(R.string.input_hint);

        // Приветственное сообщение
        showWelcomeMessage();

        // Поднимаем экран при фокусе на EditText
        userInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollToBottom();
            }
        });

        // Отправка по нажатию Enter
        userInput.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                sendMessage();
                return true;
            }
            return false;
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    123);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        voiceInputButton.setOnClickListener(v -> {
            v.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100)
                    .withEndAction(() -> {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100);
                        toggleVoiceInput();
                    });
        });
    }

    private void toggleVoiceInput() {
        if (isListening) {
            stopVoiceInput();
        } else {
            // Пересоздаем SpeechRecognizer каждый раз перед использованием
            if (speechRecognizer != null) {
                speechRecognizer.destroy();
            }
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            startVoiceInput();
        }
    }

    private void startVoiceInput() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-CN");
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getRecognitionLanguage());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите...");
//            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
//            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);

            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                private Handler timeoutHandler = new Handler();
                private Runnable timeoutRunnable = () -> stopVoiceInput();
                @Override
                public void onReadyForSpeech(Bundle params) {
                    isListening = true;
                    voiceInputButton.setImageResource(R.drawable.icon_voice_on2); // Иконка активного микрофона
                    timeoutHandler.postDelayed(timeoutRunnable, 3000);
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        userInput.setText(matches.get(0)); // Вставляем распознанный текст
                    }
                    stopVoiceInput();
                }

                @Override
                public void onError(int error) {
                    runOnUiThread(() -> {
                        stopVoiceInput();
                        // Можно добавить автоматический перезапуск при некоторых ошибках
                        if (error == SpeechRecognizer.ERROR_NO_MATCH) {
                            startVoiceInput(); // Пробуем снова, если не распознана речь
                        }
                    });
                }

                // ... другие методы интерфейса можно оставить пустыми
                @Override public void onBeginningOfSpeech() {
                    timeoutHandler.removeCallbacks(timeoutRunnable);
                }
                @Override public void onRmsChanged(float rmsdB) {}
                @Override public void onBufferReceived(byte[] buffer) {}
                @Override public void onEndOfSpeech() {}

                @Override public void onPartialResults(Bundle partialResults) {}
                @Override public void onEvent(int eventType, Bundle params) {}
            });

            speechRecognizer.startListening(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка голосового ввода", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopVoiceInput() {
        if (speechRecognizer != null) {
            try {
                speechRecognizer.stopListening();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isListening = false;
        runOnUiThread(() ->
                voiceInputButton.setImageResource(R.drawable.icon_voice_on3)
        );
    }

    private String getRecognitionLanguage() {
        // 1. Проверяем язык, выбранный в приложении (если у вас есть такой функционал)
        String appLanguage = getSelectedAppLanguage(); // Ваш метод для получения выбранного языка

        // 2. Если в приложении не выбран язык, используем системный
        if (appLanguage == null || appLanguage.isEmpty()) {
            appLanguage = Locale.getDefault().getLanguage();
        }

        // 3. Определяем код языка для распознавания речи
        switch (appLanguage) {
            case "zh":
            case "zh-CN":
            case "zh-TW":
                return "zh-CN"; // Китайский (упрощенный)

            case "ru":
            case "ru-RU":
                return "ru-RU"; // Русский

            case "en":
            case "en-US":
            case "en-GB":
                return "en-US"; // Английский (США)

            default:
                return "en-US"; // По умолчанию английский
        }
    }

    private String getSelectedAppLanguage() {
        // Пример: получаем из SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString("selected_language", "");
    }

    @Override
    public void showBottomSheet(int position) {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_message_options, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.delete_message).setOnClickListener(v -> {
            deleteMessage(position);
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.delete_all).setOnClickListener(v -> {
            deleteAllMessage();
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.cancel).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    public void deleteMessage(int position) {
        Message message = messages.get(position);
        new Thread(() -> {
            messageDao.delete(message);
            runOnUiThread(() -> {
                messages.remove(position);
                adapter.notifyItemRemoved(position);
            });
        }).start();
    }

    public void deleteAllMessage() {
        new Thread(() -> {
            messageDao.clearMessagesForUser(userId);
            runOnUiThread(() -> {
                int size = messages.size();
                messages.clear();
                adapter.notifyItemRangeRemoved(0, size);
                showWelcomeMessage();
            });
        }).start();
    }

    private void showWelcomeMessage() {
        String welcomeText = getString(R.string.welcome_message);
        messages.add(new Message(welcomeText, false));
        adapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void sendMessage() {
        String text = userInput.getText().toString().trim();
        if (text.isEmpty()) return;

        // Сохраняем сообщение пользователя
        Message userMessage = new Message(text, true);
        userMessage.setUserId(userId);
        messages.add(userMessage);
        new Thread(() -> messageDao.insert(userMessage)).start();
        adapter.notifyItemInserted(messages.size() - 1);
        userInput.setText("");
        scrollToBottom();

        // Имитация задержки ответа бота
        new Handler().postDelayed(() -> {
            String botResponse = getBotResponse(text);
            Message botMessage = new Message(botResponse, false);
            botMessage.setUserId(userId);
            messages.add(botMessage);
            new Thread(() -> messageDao.insert(botMessage)).start();
            adapter.notifyItemInserted(messages.size() - 1);
            scrollToBottom();
        }, 500);
    }

    private String getBotResponse(String question) {
        question = question.toLowerCase();
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        Locale currentLocale = config.getLocales().get(0);

        if (containsAny(question, res.getStringArray(R.array.appointment_keywords))) {
            return res.getString(R.string.response_appointment);
        } else if (containsAny(question, res.getStringArray(R.array.hello_keywords))) {
            return res.getString(R.string.response_hello);
        } else if (containsAny(question, res.getStringArray(R.array.address_keywords))) {
            return res.getString(R.string.response_address);
        } else if (containsAny(question, res.getStringArray(R.array.documents_keywords))) {
            return res.getString(R.string.response_documents);
        } else if (containsAny(question, res.getStringArray(R.array.hours_keywords))) {
            return res.getString(R.string.response_hours);
        } else if (containsAny(question, res.getStringArray(R.array.okay_keywords))) {
            return res.getString(R.string.response_okay);
        } else {
            return res.getString(R.string.response_unknown);
        }
    }

    private boolean containsAny(String input, String[] keywords) {
        for (String keyword : keywords) {
            if (input.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void scrollToBottom() {
        chatRecyclerView.post(() -> {
            if (messages.size() > 0) {
                chatRecyclerView.smoothScrollToPosition(messages.size() - 1);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopVoiceInput();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isListening) {
            startVoiceInput();
        }
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}