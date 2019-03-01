package com.tandera.core.dao.springjpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tandera.core.model.comercial.MascaraPreco;

public interface MascaraPrecoRepository extends JpaRepository<MascaraPreco, Integer> {

	public List<MascaraPreco> findByMascaraContainingIgnoreCase(String mascara);

	public MascaraPreco findById(Integer id);
	
	@Transactional
    @Modifying
    @Query("DELETE from MascaraPreco c where c.id = :id")
    public void deleteItemCategoria(@Param("id") Integer id); 

}
