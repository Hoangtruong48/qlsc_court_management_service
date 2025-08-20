package com.example.courtmanagement_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = BadmintonCourt.BadmintonCourtConstant.TABLE_NAME,
        schema = BadmintonCourt.BadmintonCourtConstant.SCHEMA)
// thông tin của sân cầu
public class BadmintonCourt {
    public static class BadmintonCourtConstant {
        public static final String TABLE_NAME = "badminton_courts";
        public static final String SCHEMA = "badminton";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String STATUS = "status";
        public static final String DESCRIPTION = "description";
        public static final String OPENING_TIME = "opening_time";
        public static final String CLOSING_TIME = "closing_time";
        public static final String TOTAL_COURTS = "total_courts";
        public static final String CONTACT_PHONE = "contact_phone";
        public static final String CONTACT_EMAIL = "contact_email";
        public static final String FLOOR_MATERIAL = "floor_material";
        public static final String LIGHTING_TYPE = "lighting_type";
        public static final String HAS_SHOWER = "has_shower";
        public static final String HAS_PARKING = "has_parking";
        public static final String HAS_DRINKS_SERVICE = "has_drinks_service";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String CREATED_BY = "created_by";
        public static final String UPDATED_BY = "updated_by";
    }

    @Transient
    public static final Map<String, String> FIELD_TO_COLUMN_MAP = new HashMap<>();
    @Transient
    public static final Map<String, String> COLUMN_TO_FIELD_MAP = new HashMap<>();

    static {
        for (Field field : BadmintonCourt.class.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                FIELD_TO_COLUMN_MAP.put(field.getName(), column.name());
                COLUMN_TO_FIELD_MAP.put(column.name(), field.getName());
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = BadmintonCourtConstant.ID)
    Integer id;
    // tên sân
    @Column(name = BadmintonCourtConstant.NAME, nullable = false)
    String name;
    // địa chỉ
    @Column(name = BadmintonCourtConstant.ADDRESS, nullable = false)
    String address;
    // mô t
    @Column(name = BadmintonCourtConstant.DESCRIPTION)
    String description;
    // thời gian mở cửa
    @Column(name = BadmintonCourtConstant.OPENING_TIME, nullable = false)
    String openingTime;
    // thời gian đóng
    @Column(name = BadmintonCourtConstant.CLOSING_TIME, nullable = false)
    String closingTime;
    // số sân
    @Column(name = BadmintonCourtConstant.TOTAL_COURTS, nullable = false)
    Integer totalCourts;
    // sdt liên hệ
    @Column(name = BadmintonCourtConstant.CONTACT_PHONE)
    String contactPhone;
    // email
    @Column(name = BadmintonCourtConstant.CONTACT_EMAIL)
    String contactEmail;

    // thông tin sàn đấu
    @Column(name = BadmintonCourtConstant.FLOOR_MATERIAL)
    String floorMaterial;
    // ánh sáng
    @Column(name = BadmintonCourtConstant.LIGHTING_TYPE)
    String lightingType;
    // có nhà tắm
    @Column(name = BadmintonCourtConstant.HAS_SHOWER)
    Boolean hasShower;
    // chỗ để xe
    @Column(name = BadmintonCourtConstant.HAS_PARKING)
    Boolean hasParking;
    // dịch vụ nước
    @Column(name = BadmintonCourtConstant.HAS_DRINKS_SERVICE)
    Boolean hasDrinksService;

    @Column(name = BadmintonCourtConstant.CREATED_AT)
    Long createdAt;

    @Column(name = BadmintonCourtConstant.UPDATED_AT)
    Long updatedAt;

    @Column(name = BadmintonCourtConstant.CREATED_BY)
    String createdBy;

    @Column(name = BadmintonCourtConstant.UPDATED_BY)
    String updatedBy;

    @Column(name = BadmintonCourtConstant.STATUS)
    Integer status;
}
