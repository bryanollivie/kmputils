# KMPUtils

A Kotlin Multiplatform utility library providing common functions for **Android**, **iOS**, and **Web (WASM)** applications.

## Utilities

### Pure Kotlin (all platforms)

| Utility | Description |
|---------|-------------|
| **CurrencyUtils** | Format and convert currencies (BRL, USD, EUR) |
| **MaskUtils** | Input masks for Phone, CPF, CNPJ, Credit Card, Date, CEP |
| **Validators** | Email, Phone, CPF (check digit), CNPJ validation |
| **CreditCardUtils** | Brand detection (Visa, Mastercard, Elo...), Luhn validation, CVV, expiry, installments |
| **CryptoUtils** | XOR encrypt/decrypt, FNV hash, Base64, token generation |
| **ColorUtils** | Hex↔RGB, darken, lighten, contrast ratio, WCAG accessibility |
| **DateUtils** | Format dates, relative time ("2 hours ago") |
| **StringUtils** | Capitalize, truncate, remove accents, slug |
| **FormValidation** | Composable validation engine with chainable rules |
| **StateMachine** | Generic FSM with typed transitions and history |
| **LruCache** | In-memory cache with LRU eviction |
| **EventBus** | Decoupled communication via SharedFlow |
| **RateLimiter** | Call rate control with time window |
| **DebounceThrottle** | Debounce and throttle for inputs |
| **RetryStrategy** | Exponential and linear backoff |

### Platform-Specific (expect/actual)

| Function | Android | iOS | Web |
|----------|---------|-----|-----|
| `hideKeyboard()` | InputMethodManager | resignFirstResponder | document.activeElement.blur() |
| `isNetworkAvailable()` | ConnectivityManager | always true (simplified) | navigator.onLine |
| `copyToClipboard()` | ClipboardManager | UIPasteboard | navigator.clipboard |
| `isDarkMode()` | Configuration.UI_MODE_NIGHT | UITraitCollection | matchMedia prefers-color-scheme |
| `getDeviceInfo()` | Build.MODEL/VERSION | UIDevice | navigator.userAgent |
| `hapticFeedback()` | Vibrator/VibrationEffect | UIImpactFeedbackGenerator | N/A |

## Setup

### Android

Initialize in your `Application` or `Activity`:

```kotlin
KMPUtilsContext.init(application)
```

### Usage

```kotlin
// Currency
CurrencyUtils.format(1500.0, CurrencyCode.BRL) // "R$ 1.500,00"
CurrencyUtils.convert(100.0, CurrencyCode.USD, CurrencyCode.BRL) // 520.0

// Masks
MaskUtils.maskCpf("12345678901") // "123.456.789-01"
MaskUtils.maskPhone("11999887766") // "(11) 99988-7766"

// Validators
Validators.isValidCpf("123.456.789-09") // true/false (check digit)
Validators.isValidEmail("user@email.com") // true

// Credit Card
CreditCardUtils.detectBrand("4111111111111111") // CardBrand.VISA
CreditCardUtils.isValidNumber("4111111111111111") // true (Luhn)

// Encryption
CryptoUtils.encrypt("hello", "key") // hex string
CryptoUtils.base64Encode("hello") // "aGVsbG8="

// Form Validation
val validator = FormValidator()
    .addField("email", FormRules.required(), FormRules.email())
    .addField("cpf", FormRules.required(), FormRules.cpf())
validator.isFormValid(mapOf("email" to "a@b.com", "cpf" to "12345678909"))

// State Machine
val machine = stateMachine<String, String>("Idle") {
    transition("Idle", "START", "Loading")
    transition("Loading", "SUCCESS", "Done")
}
machine.send("START") // state -> "Loading"

// Platform
isNetworkAvailable() // true/false
copyToClipboard("text")
hapticFeedback()
```

## Tech Stack

| Technology | Version |
|------------|---------|
| Kotlin | 2.1.0 |
| Compose Multiplatform | 1.7.3 |
| kotlinx-datetime | 0.6.1 |
| kotlinx-coroutines | 1.9.0 |

**Targets**: Android (API 26+), iOS (arm64, x64, simulator), Web (WASM/JS)

## License

MIT
