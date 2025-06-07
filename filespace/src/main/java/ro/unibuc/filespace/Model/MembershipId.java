package ro.unibuc.filespace.Model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MembershipId implements Serializable {
    private Long groupId;
    private Long userId;
}
