package ro.unibuc.filespace.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@Entity
@Table(name = "MEMBERSHIP")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@IdClass(MembershipId.class)
public class Membership {
    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}