package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.authorization.database.entity.Role;
import ua.everybuy.authorization.database.entity.RoleList;
import ua.everybuy.authorization.database.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName(RoleList.USER).get();
    }

    public Role getAdminRole() {
        return roleRepository.findByName(RoleList.ADMIN).get();
    }
}
