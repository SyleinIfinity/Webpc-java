package com.webpc.be.modules.catalog.repository;

import com.webpc.be.modules.catalog.entity.DanhMuc;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {

    @EntityGraph(attributePaths = {"danhMucCha", "danhMucCons"})
    List<DanhMuc> findAllByOrderByMaDanhMucAsc();

    @EntityGraph(attributePaths = {"danhMucCha", "danhMucCons"})
    Optional<DanhMuc> findByMaDanhMuc(Integer maDanhMuc);
}
