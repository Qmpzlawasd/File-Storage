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
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
