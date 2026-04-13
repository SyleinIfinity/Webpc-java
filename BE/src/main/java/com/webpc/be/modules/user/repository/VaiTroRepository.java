package com.webpc.be.modules.user.repository;

import com.webpc.be.modules.user.entity.VaiTro;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaiTroRepository extends JpaRepository<VaiTro, Integer> {

    Optional<VaiTro> findByTenVaiTro(String tenVaiTro);
}
