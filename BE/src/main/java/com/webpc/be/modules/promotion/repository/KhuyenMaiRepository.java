package com.webpc.be.modules.promotion.repository;

import com.webpc.be.modules.promotion.entity.KhuyenMai;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {

    Optional<KhuyenMai> findByMaCodeKM(String maCodeKM);
}

