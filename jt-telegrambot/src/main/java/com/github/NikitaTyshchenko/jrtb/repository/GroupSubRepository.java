package com.github.NikitaTyshchenko.jrtb.repository;

import com.github.NikitaTyshchenko.jrtb.repository.entity.GroupSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupSubRepository extends JpaRepository<GroupSub, Integer> {
}
