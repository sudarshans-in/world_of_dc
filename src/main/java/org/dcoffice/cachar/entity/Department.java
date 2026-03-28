package org.dcoffice.cachar.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Department enum for complaint assignment and management
 *
 * @author District Cachar IT Team
 */
public enum Department {
    
    // Core Administrative Departments
    DISTRICT_ADMINISTRATION("District Administration", "Overall district administration"),
    DISTRICT_ADMINISTRATION_DEPARTMENT("District Administration", "Overall district administration"),
    REVENUE_DEPARTMENT("Revenue Department", "Land records, revenue collection, and property matters"),
    COLLECTORATE("Collectorate", "District collector's office"),
    COLLECTORATE_DEPARTMENT("Collectorate", "District collector's office"),
    GENERAL_ADMINISTRATION("General Administration", "General administrative services"),

    // Infrastructure Departments
    PUBLIC_WORKS_DEPARTMENT("Public Works Department (PWD)", "Roads, bridges, and infrastructure development"),
    PWD("Public Works Department (PWD)", "Roads, bridges, and infrastructure development"),
    WATER_RESOURCES("Water Resources", "Water supply, irrigation, and water management"),
    WATER_RESOURCES_DEPARTMENT("Water Resources", "Water supply, irrigation, and water management"),
    ELECTRICITY_DEPARTMENT("Electricity Department", "Power supply and electrical infrastructure"),

    // Social Services
    HEALTH_DEPARTMENT("Health Department", "Healthcare services and medical facilities"),
    EDUCATION_DEPARTMENT("Education Department", "Schools, colleges, and educational services"),
    SOCIAL_WELFARE("Social Welfare", "Social security and welfare programs"),
    SOCIAL_WELFARE_DEPARTMENT("Social Welfare", "Social security and welfare programs"),

    // Public Services
    POLICE_DEPARTMENT("Police Department", "Law enforcement and public safety"),
    FIRE_SERVICES("Fire Services", "Fire safety and emergency response"),
    FIRE_DEPARTMENT("Fire Department", "Fire safety and emergency response"),
    FIRE_SERVICES_DEPARTMENT("Fire Services", "Fire safety and emergency response"),
    TRANSPORT_DEPARTMENT("Transport Department", "Public transportation and vehicle registration"),

    // Development and Planning
    RURAL_DEVELOPMENT("Rural Development", "Rural infrastructure and development programs"),
    RURAL_DEVELOPMENT_DEPARTMENT("Rural Development", "Rural infrastructure and development programs"),
    URBAN_DEVELOPMENT("Urban Development", "Urban planning and municipal services"),
    URBAN_DEVELOPMENT_DEPARTMENT("Urban Development", "Urban planning and municipal services"),
    AGRICULTURE_DEPARTMENT("Agriculture Department", "Agricultural services and farmer support"),

    // Environmental and Utilities
    ENVIRONMENT_DEPARTMENT("Environment Department", "Environmental protection and conservation"),
    SANITATION_DEPARTMENT("Sanitation Department", "Waste management and sanitation services"),
    FOREST_DEPARTMENT("Forest Department", "Forest conservation and wildlife management"),

    // Specialized Services
    FOOD_AND_SUPPLIES("Food and Supplies", "Public distribution system and food security"),
    FOOD_AND_SUPPLIES_DEPARTMENT("Food and Supplies", "Public distribution system and food security"),
    LABOUR_DEPARTMENT("Labour Department", "Labour welfare and employment services"),
    WOMEN_AND_CHILD_DEVELOPMENT("Women and Child Development", "Women and child welfare programs"),
    WOMEN_AND_CHILD_DEVELOPMENT_DEPARTMENT("Women and Child Development", "Women and child welfare programs"),

    // Technical Departments
    INFORMATION_TECHNOLOGY("Information Technology", "IT services and digital governance"),
    INFORMATION_TECHNOLOGY_DEPARTMENT("Information Technology", "IT services and digital governance"),
    STATISTICS_DEPARTMENT("Statistics Department", "Data collection and statistical services"),

    // Municipal and Local Bodies
    MUNICIPAL_CORPORATION("Municipal Corporation", "Urban local body"),
    MUNICIPALITY("Municipality", "Municipal services"),
    MUNICIPAL_DEPARTMENT("Municipal Department", "Municipal services"),
    DISTRICT_COUNCIL("District Council", "District council administration"),
    PANCHAYAT("Panchayat", "Gram panchayat administration"),
    PANCHAYATI_RAJ("Panchayati Raj", "Panchayati raj institutions"),
    PANCHAYAT_AND_RURAL_DEVELOPMENT("Panchayat and Rural Development", "Panchayat and rural development"),

    // Finance and Revenue
    FINANCE_DEPARTMENT("Finance Department", "Finance and budgeting"),
    LAND_REVENUE("Land Revenue", "Land revenue and records"),
    LAND_RECORDS("Land Records", "Land records management"),
    TAX_DEPARTMENT("Tax Department", "Tax collection and assessment"),

    // Development and Welfare
    FISHERIES_DEPARTMENT("Fisheries Department", "Fisheries and aquaculture"),
    ANIMAL_HUSBANDRY_DEPARTMENT("Animal Husbandry", "Animal husbandry and veterinary"),
    ANIMAL_HUSBANDRY("Animal Husbandry", "Animal husbandry and veterinary"),
    INDUSTRIES_DEPARTMENT("Industries Department", "Industrial development"),
    TOURISM_DEPARTMENT("Tourism Department", "Tourism promotion"),
    TOURISM("Tourism", "Tourism promotion"),
    SPORTS_DEPARTMENT("Sports Department", "Sports and youth affairs"),
    SPORTS("Sports", "Sports and youth affairs"),
    CULTURAL_AFFAIRS("Cultural Affairs", "Culture and heritage"),
    CULTURAL_AFFAIRS_DEPARTMENT("Cultural Affairs", "Culture and heritage"),
    MINORITIES_WELFARE("Minorities Welfare", "Welfare of minorities"),
    TRIBAL_WELFARE("Tribal Welfare", "Welfare of tribal communities"),
    BORDER_AREAS_DEVELOPMENT("Border Areas Development", "Development of border areas"),

    // Emergency and Safety
    DISASTER_MANAGEMENT("Disaster Management", "Disaster management and relief"),
    DISASTER_MANAGEMENT_DEPARTMENT("Disaster Management", "Disaster management and relief"),
    MINES_AND_MINERALS("Mines and Minerals", "Mining and mineral resources"),
    SERICULTURE("Sericulture", "Silk worm and sericulture"),
    HANDLOOM("Handloom", "Handloom and textiles"),
    PLANNING_DEPARTMENT("Planning Department", "Planning and development"),

    // Other
    OTHER("Other", "Other departments not specifically listed"),
    @JsonEnumDefaultValue
    UNASSIGNED("Unassigned", "Not yet assigned to any department");
    
    private final String displayName;
    private final String description;
    
    Department(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}

