package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.filespace.Model.FileId;
import ro.unibuc.filespace.Model.Membership;
import ro.unibuc.filespace.Model.MembershipId;
import ro.unibuc.filespace.Model.Storage;

@Repository
public interface MembershipRepository extends CrudRepository<Membership, MembershipId> {

}