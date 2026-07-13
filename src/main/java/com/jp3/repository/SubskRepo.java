package com.jp3.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jp3.entity.Subsk;

@Repository
public interface SubskRepo extends JpaRepository<Subsk, Long> {

	//	とりまユーザーで検索する
	List<Subsk> findByUserId(String userId);

	//	更新日とユーザーで検索する
	List<Subsk> findByUpdateAtAndUserId(LocalDate date, String userId);

	//サブスクのIDとユーザーで検索する
	List<Subsk> findBySubskIdInAndUserId(List<Long> subskId, String userId);
	
	
	Optional<Subsk> findBySubskIdAndUserId(Long subskId, String userId);

}
