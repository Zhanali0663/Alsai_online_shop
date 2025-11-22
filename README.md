# 🛍️ Alsai - Online Shopping App

> Modern e-commerce Android application with real-time synchronization and secure authentication

[English](#english) | [Русский](#russian)

---

<a name="english"></a>
## 🇬🇧 English

### 📱 About

Alsai is a feature-rich online shopping application built with modern Android technologies. Browse products, manage your cart, and make purchases seamlessly with real-time data synchronization and secure user authentication.

### ✨ Key Features

- 🛒 **Shopping Cart** - Easy product selection and cart management
- 🔐 **Secure Authentication** - Multiple sign-in options including Google Sign-In
- 📦 **Product Catalog** - Browse through various categories and products
- 💳 **Order Management** - Track your orders and purchase history
- 💾 **Real-time Sync** - Instant data synchronization across devices
- 🎨 **Material Design** - Beautiful, intuitive user interface
- ⚡ **High Performance** - Optimized for speed and smooth scrolling
- 🔍 **Product Search** - Quickly find what you're looking for

### 🛠️ Tech Stack

#### Core Android Components

| Technology | Purpose |
|------------|---------|
| **AndroidX Core KTX** | Kotlin extensions for core Android APIs |
| **AppCompat** | Backward compatibility for older Android versions |
| **Material Components** | Material Design UI components |
| **Activity** | Base component for app screens |
| **ConstraintLayout** | Flexible layout manager for complex UIs |
| **Fragment** | Modular and reusable UI components |
| **RecyclerView** | Efficient display of product lists |
| **Navigation Component** | Simplified navigation between screens |

#### Authentication

- **Firebase Authentication** - User authentication with multiple providers
- **AndroidX Credentials** - Secure credential storage and retrieval
- **Google Identity Services** - Google account authentication API

#### Database

- **Firebase Realtime Database** - Cloud-hosted NoSQL database for products, orders, and user data with real-time synchronization

#### Image Loading

- **Glide** - Fast and efficient product image loading and caching

#### Build & Language

- **Kotlin** - Official language for Android development
- **Gradle** - Build automation system
- **ViewBinding** - Simplified view interaction in layouts

### 🎯 App Architecture

- **MVVM Pattern** - Clean separation of concerns
- **Repository Pattern** - Data management abstraction
- **LiveData** - Reactive data handling
- **Navigation Component** - Single-activity architecture

### 📋 Requirements

- Android SDK 21+
- Kotlin 1.9+
- Gradle 8.0+
- Active internet connection for real-time features

### 🚀 Getting Started

1. Clone the repository
```bash
git clone https://github.com/Zhanali0663/Alsai_online_shop/
```

2. Open the project in Android Studio

3. Sync Gradle dependencies

4. Configure Firebase:
   - Add `google-services.json` to the `app/` directory
   - Enable Authentication and Realtime Database in Firebase Console
   - Set up database structure for products and orders

5. Run the application

### 📦 Features in Development

- [ ] Payment gateway integration
- [ ] Wishlist functionality
- [ ] Product reviews and ratings
- [ ] Push notifications for order updates
- [ ] Multi-language support

### 📄 License

MIT License

### 👥 Contributors

Zhanali0663

---

<a name="russian"></a>
## 🇷🇺 Русский

### 📱 О проекте

Alsai — это многофункциональное приложение для онлайн-покупок, созданное с использованием современных Android-технологий. Просматривайте товары, управляйте корзиной и совершайте покупки с синхронизацией в реальном времени и безопасной аутентификацией.

### ✨ Ключевые возможности

- 🛒 **Корзина покупок** - Простой выбор товаров и управление корзиной
- 🔐 **Безопасная аутентификация** - Несколько вариантов входа, включая Google
- 📦 **Каталог товаров** - Просмотр различных категорий и товаров
- 💳 **Управление заказами** - Отслеживание заказов и истории покупок
- 💾 **Синхронизация в реальном времени** - Мгновенная синхронизация данных между устройствами
- 🎨 **Material Design** - Красивый и интуитивный интерфейс
- ⚡ **Высокая производительность** - Оптимизирован для скорости и плавной прокрутки
- 🔍 **Поиск товаров** - Быстро находите то, что ищете

### 🛠️ Технологический стек

#### Основные компоненты Android

| Технология | Назначение |
|------------|------------|
| **AndroidX Core KTX** | Расширения Kotlin для основных API Android |
| **AppCompat** | Обратная совместимость для старых версий Android |
| **Material Components** | Компоненты UI в стиле Material Design |
| **Activity** | Базовый компонент для создания экранов |
| **ConstraintLayout** | Гибкий менеджер макетов для сложных интерфейсов |
| **Fragment** | Модульные и переиспользуемые компоненты UI |
| **RecyclerView** | Эффективное отображение списков товаров |
| **Navigation Component** | Упрощённая навигация между экранами |

#### Аутентификация

- **Firebase Authentication** - Аутентификация пользователей с различными провайдерами
- **AndroidX Credentials** - Безопасное хранение и извлечение учётных данных
- **Google Identity Services** - API для аутентификации через Google

#### База данных

- **Firebase Realtime Database** - Облачная NoSQL база данных для товаров, заказов и пользовательских данных с синхронизацией в реальном времени

#### Загрузка изображений

- **Glide** - Быстрая и эффективная загрузка и кэширование изображений товаров

#### Сборка и язык

- **Kotlin** - Официальный язык для разработки под Android
- **Gradle** - Система автоматизации сборки
- **ViewBinding** - Упрощённое взаимодействие с представлениями

### 🎯 Архитектура приложения

- **MVVM паттерн** - Чёткое разделение ответственности
- **Repository паттерн** - Абстракция управления данными
- **LiveData** - Реактивная обработка данных
- **Navigation Component** - Архитектура с одной Activity

### 📋 Требования

- Android SDK 21+
- Kotlin 1.9+
- Gradle 8.0+
- Активное интернет-соединение для функций реального времени

### 🚀 Начало работы

1. Клонируйте репозиторий
```bash
git clone https://github.com/Zhanali0663/Alsai_online_shop/
```

2. Откройте проект в Android Studio

3. Синхронизируйте зависимости Gradle

4. Настройте Firebase:
   - Добавьте `google-services.json` в директорию `app/`
   - Включите Authentication и Realtime Database в консоли Firebase
   - Настройте структуру базы данных для товаров и заказов

5. Запустите приложение

### 📦 Функции в разработке

- [ ] Интеграция платёжного шлюза
- [ ] Функция списка желаний
- [ ] Отзывы и рейтинги товаров
- [ ] Push-уведомления об обновлении заказов
- [ ] Поддержка нескольких языков

### 📄 Лицензия

MIT License

### 👥 Участники

Zhanali0663

---

<div align="center">

**Сделано с ❤️**

[![GitHub stars](https://img.shields.io/github/stars/Zhanali0663/Alsai_online_shop?style=social)](https://github.com/Zhanali0663/Alsai_online_shop)
[![GitHub forks](https://img.shields.io/github/forks/Zhanali0663/Alsai_online_shop?style=social)](https://github.com/Zhanali0663/Alsai_online_shop)

</div>
