# 🏥 Поликлиника - Мобильное приложение для записи на прием

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Room](https://img.shields.io/badge/Room-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-blue?style=for-the-badge)

**Инновационное решение для цифровизации медицинских услуг**  
*Сокращаем очереди, упрощаем запись, улучшаем сервис*

</div>

## 🌟 О проекте

**Поликлиника** - это современное мобильное приложение для Android, разработанное в рамках курсовой работы, которое революционизирует процесс записи пациентов на прием к врачам. Приложение сочетает в себе передовые технологии и удобный интерфейс для создания бесшовного опыта взаимодействия между пациентами и медицинскими учреждениями.

### 🎯 Ключевые преимущества

| Для пациентов | Для врачей | Для администрации |
|---------------|------------|-------------------|
| ✅ Мгновенная онлайн-запись | ✅ Управление расписанием | ✅ Снижение нагрузки |
| ✅ Мультиязычный интерфейс | ✅ Электронная очередь | ✅ Аналитика загруженности |
| ✅ Голосовой помощник | ✅ Просмотр пациентов | ✅ Оптимизация процессов |
| ✅ QR-талоны | ✅ Медицинские назначения | ✅ Цифровая трансформация |

## 🚀 Уникальные особенности

### 🌐 Мультиязычная поддержка
- **Русский, Английский, Китайский** языки интерфейса
- **Голосовой помощник** с распознаванием речи на 3 языках
- Полная локализация для иностранных пациентов

### 📱 Продвинутые технологии
- **QR-генерация талонов** для быстрой идентификации
- **Офлайн-режим** с локальной базой данных Room
- **Умные уведомления** о предстоящих приемах
- **Безопасное хранение** медицинских данных

### 🏗 Архитектурное превосходство
- **Чистая архитектура MVVM** для масштабируемости
- **LiveData и ViewModel** для реактивного программирования
- **Room Database** с полной SQL-поддержкой
- **Material Design 3** для современного UI/UX

## 📊 Сравнение с аналогами

| Функция | Госуслуги.Здоровье | ЕМИАС | Наше решение |
|---------|-------------------|--------|--------------|
| Мультиязычность | ❌ | ❌ | ✅ **3 языка** |
| Голосовой помощник | ❌ | ❌ | ✅ **Полная поддержка** |
| QR-идентификация | ❌ | ⚠️ Частично | ✅ **Полный цикл** |
| Офлайн-режим | ❌ | ❌ | ✅ **Room Database** |
| Сложность интерфейса | 🔴 Высокая | 🟡 Средняя | 🟢 **Низкая** |

## 🛠 Технологический стек

### Основные технологии
- **Язык программирования:** Java 11
- **Платформа:** Android 9.0+ (API 28)
- **Архитектура:** MVVM (Model-View-ViewModel)
- **База данных:** Room 2.5 + SQLite
- **Сборка:** Android Studio 2023

### Ключевые библиотеки
```java
// Архитектурные компоненты
implementation 'androidx.room:room-runtime:2.5.0'
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.6.0'

// Голосовой ввод
implementation 'androidx.speech:speech:1.0.0'

// QR-генерация
implementation 'com.google.zxing:core:3.4.1'

// Material Design
implementation 'com.google.android.material:material:1.9.0'
```

## 📸 Скриншоты

<div align="center">

<img src="https://github.com/Yusi-21/Healthcare/raw/main/app/src/main/res/screenshots/Screenshot1.jpg" width="30%" alt="login"/>
<img src="https://github.com/Yusi-21/Healthcare/raw/main/app/src/main/res/drawable/screenshot2_black.jpg" width="30%" alt="Все законы_2"/>

*Главный экран и пример закона*
</div>


<div align="center">
  
<img src="https://github.com/Yusi-21/48-Laws-Of-Power/raw/main/app/src/main/res/drawable/screenshot1_white.jpg" width="30%" alt="Закон 1_1"/>
<img src="https://github.com/Yusi-21/48-Laws-Of-Power/raw/main/app/src/main/res/drawable/screenshot3_white.jpg" width="30%" alt="Все законы_2"/>

*Главный экран и пример закона*

</div>


## 🚀 Установка и использование

### ⚙ Установка
- Клонируйте репозиторий:
```bash
git clone https://github.com/Yusi-21/Healthcare.git
```
- Откройте проект в Android Studio
- Соберите и запустите приложение
