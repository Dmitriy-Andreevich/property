package space.meduzza.property.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

@Data
@NoArgsConstructor
@MappedSuperclass
class BaseEntity {
    @Id
    @GeneratedValue
    private int id;
    private Timestamp createTime;
    private Timestamp updateTime;

    @PrePersist
    private void onCreate(){
        createTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
        onUpdate();
    }

    @PreUpdate
    private void onUpdate(){
        updateTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
    }
}
