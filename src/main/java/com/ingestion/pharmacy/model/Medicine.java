package com.ingestion.pharmacy.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "medicines")
public class Medicine extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "generic_name")
    private String genericName;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "medicine_code", unique = true)
    private String medicineCode;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "dosage_form")
    @Enumerated(EnumType.STRING)
    private DosageForm dosageForm;

    @Column(name = "strength")
    private String strength;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "category")
    private String category;

    @Column(name = "sub_category")
    private String subCategory;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    @Column(name = "package_size")
    private Integer packageSize;

    @Column(name = "package_type")
    private String packageType;

    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "selling_price", precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;

    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "is_prescription_required")
    private Boolean isPrescriptionRequired = false;

    @Column(name = "is_controlled_substance")
    private Boolean isControlledSubstance = false;

    @Column(name = "controlled_substance_category")
    private String controlledSubstanceCategory;

    @Column(name = "storage_instructions", length = 500)
    private String storageInstructions;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "barcode")
    private String barcode;

    @ElementCollection
    @CollectionTable(name = "medicine_ingredients", 
                    joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "ingredient")
    private Set<String> ingredients = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "medicine_side_effects", 
                    joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "side_effect")
    private Set<String> sideEffects = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "medicine_contraindications", 
                    joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "contraindication")
    private Set<String> contraindications = new HashSet<>();

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MedicineStock> stocks = new HashSet<>();

    public enum DosageForm {
        TABLET, CAPSULE, SYRUP, INJECTION, CREAM, OINTMENT, GEL, DROPS, INHALER, POWDER, SUSPENSION, SOLUTION, SUPPOSITORY, PATCH, OTHER
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DosageForm getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(DosageForm dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Integer getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(Integer packageSize) {
        this.packageSize = packageSize;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Boolean getIsPrescriptionRequired() {
        return isPrescriptionRequired;
    }

    public void setIsPrescriptionRequired(Boolean prescriptionRequired) {
        isPrescriptionRequired = prescriptionRequired;
    }

    public Boolean getIsControlledSubstance() {
        return isControlledSubstance;
    }

    public void setIsControlledSubstance(Boolean controlledSubstance) {
        isControlledSubstance = controlledSubstance;
    }

    public String getControlledSubstanceCategory() {
        return controlledSubstanceCategory;
    }

    public void setControlledSubstanceCategory(String controlledSubstanceCategory) {
        this.controlledSubstanceCategory = controlledSubstanceCategory;
    }

    public String getStorageInstructions() {
        return storageInstructions;
    }

    public void setStorageInstructions(String storageInstructions) {
        this.storageInstructions = storageInstructions;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Set<String> getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(Set<String> sideEffects) {
        this.sideEffects = sideEffects;
    }

    public Set<String> getContraindications() {
        return contraindications;
    }

    public void setContraindications(Set<String> contraindications) {
        this.contraindications = contraindications;
    }

    public Set<MedicineStock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<MedicineStock> stocks) {
        this.stocks = stocks;
    }

    // Helper methods
    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public void removeIngredient(String ingredient) {
        ingredients.remove(ingredient);
    }

    public void addSideEffect(String sideEffect) {
        sideEffects.add(sideEffect);
    }

    public void removeSideEffect(String sideEffect) {
        sideEffects.remove(sideEffect);
    }

    public void addContraindication(String contraindication) {
        contraindications.add(contraindication);
    }

    public void removeContraindication(String contraindication) {
        contraindications.remove(contraindication);
    }

    public void addStock(MedicineStock stock) {
        stocks.add(stock);
        stock.setMedicine(this);
    }

    public void removeStock(MedicineStock stock) {
        stocks.remove(stock);
        stock.setMedicine(null);
    }

    public Integer getTotalStock() {
        return stocks.stream()
                .filter(stock -> !stock.getIsExpired())
                .mapToInt(MedicineStock::getCurrentQuantity)
                .sum();
    }

    public boolean isLowStock() {
        return getTotalStock() <= reorderLevel;
    }
}