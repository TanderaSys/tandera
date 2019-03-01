package com.tandera.core.dao.springjpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tandera.core.model.comercial.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {

	public List<Marca> findByDescrContainingIgnoreCase(String descr);

	public Marca findById(Integer id);
	
	@Transactional
    @Modifying
    @Query("DELETE from Marca c where c.id = :id")
    public void deleteItemCategoria(@Param("id") Integer id); 

}
