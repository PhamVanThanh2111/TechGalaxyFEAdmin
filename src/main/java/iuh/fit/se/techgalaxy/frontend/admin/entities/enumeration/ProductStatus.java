package iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration;

public enum ProductStatus {
    AVAILABLE("Available"),
    OUT_OF_STOCK("Out of Stock"),
    DISCONTINUED("Discontinued");

    private final String displayName;

    ProductStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ProductStatus fromString(String value) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.name().equalsIgnoreCase(value) || status.getDisplayName().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ProductStatus value: " + value);
    }
}
