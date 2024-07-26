/**
 * Модель для представления результатов инференса
 */
data class InferringResult(
    /**
     * Персоны в тексте
     */
    var persons: String = "",
    /**
     * Локации в тексте
     */
    var locations: String = "",
    /**
     * Организации в тексте
     */
    var organizations: String = "",
    /**
     * Остальные значимые элементы в тексте
     */
    var misc: String = "",
)
