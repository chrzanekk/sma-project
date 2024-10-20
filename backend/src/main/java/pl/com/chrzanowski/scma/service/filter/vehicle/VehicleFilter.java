package pl.com.chrzanowski.scma.service.filter.vehicle;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class VehicleFilter {

    private Long id;
    private Long brandId;
    private String brandName;
    private Long modelId;
    private String modelName;
    private String registrationNumber;
    private String vin;
    private Short productionYearStartWith;
    private Short productionYearEndWith;
    private LocalDate firstRegistrationDateStartWith;
    private LocalDate firstRegistrationDateEndWith;
    private Short freePlacesForTechInspectionStartWith;
    private Short freePlacesForTechInspectionEndWith;
    private Long fuelTypeId;
    private String fuelTypeName;
    private Long vehicleTypeId;
    private String vehicleTypeName;
    private BigDecimal lengthStartWith;
    private BigDecimal lengthEndWith;
    private BigDecimal widthStartWith;
    private BigDecimal widthEndWith;
    private BigDecimal heightStartWith;
    private BigDecimal heightEndWith;
    private Instant createDateStartWith;
    private Instant createDateEndWith;
    private Instant modifyDateStartWith;
    private Instant modifyDateEndWith;

    public VehicleFilter() {
    }

    public VehicleFilter(Long id,
                         Long brandId,
                         String brandName,
                         Long modelId,
                         String modelName,
                         String registrationNumber,
                         String vin,
                         Short productionYearStartWith,
                         Short productionYearEndWith,
                         LocalDate firstRegistrationDateStartWith,
                         LocalDate firstRegistrationDateEndWith,
                         Short freePlacesForTechInspectionStartWith,
                         Short freePlacesForTechInspectionEndWith,
                         Long fuelTypeId,
                         String fuelTypeName,
                         Long vehicleTypeId,
                         String vehicleTypeName,
                         BigDecimal lengthStartWith,
                         BigDecimal lengthEndWith,
                         BigDecimal widthStartWith,
                         BigDecimal widthEndWith,
                         BigDecimal heightStartWith,
                         BigDecimal heightEndWith,
                         Instant createDateStartWith,
                         Instant createDateEndWith,
                         Instant modifyDateStartWith,
                         Instant modifyDateEndWith) {
        this.id = id;
        this.brandId = brandId;
        this.brandName = brandName;
        this.modelId = modelId;
        this.modelName = modelName;
        this.registrationNumber = registrationNumber;
        this.vin = vin;
        this.productionYearStartWith = productionYearStartWith;
        this.productionYearEndWith = productionYearEndWith;
        this.firstRegistrationDateStartWith = firstRegistrationDateStartWith;
        this.firstRegistrationDateEndWith = firstRegistrationDateEndWith;
        this.freePlacesForTechInspectionStartWith = freePlacesForTechInspectionStartWith;
        this.freePlacesForTechInspectionEndWith = freePlacesForTechInspectionEndWith;
        this.fuelTypeId = fuelTypeId;
        this.fuelTypeName = fuelTypeName;
        this.vehicleTypeId = vehicleTypeId;
        this.vehicleTypeName = vehicleTypeName;
        this.lengthStartWith = lengthStartWith;
        this.lengthEndWith = lengthEndWith;
        this.widthStartWith = widthStartWith;
        this.widthEndWith = widthEndWith;
        this.heightStartWith = heightStartWith;
        this.heightEndWith = heightEndWith;
        this.createDateStartWith = createDateStartWith;
        this.createDateEndWith = createDateEndWith;
        this.modifyDateStartWith = modifyDateStartWith;
        this.modifyDateEndWith = modifyDateEndWith;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Short getProductionYearStartWith() {
        return productionYearStartWith;
    }

    public void setProductionYearStartWith(Short productionYearStartWith) {
        this.productionYearStartWith = productionYearStartWith;
    }

    public Short getProductionYearEndWith() {
        return productionYearEndWith;
    }

    public void setProductionYearEndWith(Short productionYearEndWith) {
        this.productionYearEndWith = productionYearEndWith;
    }

    public LocalDate getFirstRegistrationDateStartWith() {
        return firstRegistrationDateStartWith;
    }

    public void setFirstRegistrationDateStartWith(LocalDate firstRegistrationDateStartWith) {
        this.firstRegistrationDateStartWith = firstRegistrationDateStartWith;
    }

    public LocalDate getFirstRegistrationDateEndWith() {
        return firstRegistrationDateEndWith;
    }

    public void setFirstRegistrationDateEndWith(LocalDate firstRegistrationDateEndWith) {
        this.firstRegistrationDateEndWith = firstRegistrationDateEndWith;
    }

    public Short getFreePlacesForTechInspectionStartWith() {
        return freePlacesForTechInspectionStartWith;
    }

    public void setFreePlacesForTechInspectionStartWith(Short freePlacesForTechInspectionStartWith) {
        this.freePlacesForTechInspectionStartWith = freePlacesForTechInspectionStartWith;
    }

    public Short getFreePlacesForTechInspectionEndWith() {
        return freePlacesForTechInspectionEndWith;
    }

    public void setFreePlacesForTechInspectionEndWith(Short freePlacesForTechInspectionEndWith) {
        this.freePlacesForTechInspectionEndWith = freePlacesForTechInspectionEndWith;
    }

    public Long getFuelTypeId() {
        return fuelTypeId;
    }

    public void setFuelTypeId(Long fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public String getFuelTypeName() {
        return fuelTypeName;
    }

    public void setFuelTypeName(String fuelTypeName) {
        this.fuelTypeName = fuelTypeName;
    }

    public Long getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(Long vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public BigDecimal getLengthStartWith() {
        return lengthStartWith;
    }

    public void setLengthStartWith(BigDecimal lengthStartWith) {
        this.lengthStartWith = lengthStartWith;
    }

    public BigDecimal getLengthEndWith() {
        return lengthEndWith;
    }

    public void setLengthEndWith(BigDecimal lengthEndWith) {
        this.lengthEndWith = lengthEndWith;
    }

    public BigDecimal getWidthStartWith() {
        return widthStartWith;
    }

    public void setWidthStartWith(BigDecimal widthStartWith) {
        this.widthStartWith = widthStartWith;
    }

    public BigDecimal getWidthEndWith() {
        return widthEndWith;
    }

    public void setWidthEndWith(BigDecimal widthEndWith) {
        this.widthEndWith = widthEndWith;
    }

    public BigDecimal getHeightStartWith() {
        return heightStartWith;
    }

    public void setHeightStartWith(BigDecimal heightStartWith) {
        this.heightStartWith = heightStartWith;
    }

    public BigDecimal getHeightEndWith() {
        return heightEndWith;
    }

    public void setHeightEndWith(BigDecimal heightEndWith) {
        this.heightEndWith = heightEndWith;
    }

    public Instant getCreateDateStartWith() {
        return createDateStartWith;
    }

    public void setCreateDateStartWith(Instant createDateStartWith) {
        this.createDateStartWith = createDateStartWith;
    }

    public Instant getCreateDateEndWith() {
        return createDateEndWith;
    }

    public void setCreateDateEndWith(Instant createDateEndWith) {
        this.createDateEndWith = createDateEndWith;
    }

    public Instant getModifyDateStartWith() {
        return modifyDateStartWith;
    }

    public void setModifyDateStartWith(Instant modifyDateStartWith) {
        this.modifyDateStartWith = modifyDateStartWith;
    }

    public Instant getModifyDateEndWith() {
        return modifyDateEndWith;
    }

    public void setModifyDateEndWith(Instant modifyDateEndWith) {
        this.modifyDateEndWith = modifyDateEndWith;
    }



}
