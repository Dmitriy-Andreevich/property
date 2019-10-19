package space.meduzza.property.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "medias")
public class MediaEntity extends BaseEntity {
    private String link;
    @ManyToOne
    @JoinColumn
    private PropertyEntity property;
}
