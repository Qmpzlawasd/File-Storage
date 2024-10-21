package ro.unibuc.filespace.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MembershipId implements Serializable {
    private Group group;
    private User user;
}
