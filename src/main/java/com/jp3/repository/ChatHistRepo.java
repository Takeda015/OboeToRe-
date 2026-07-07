//会話をためておくchat_historiesのリポジトリ
package com.jp3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jp3.entity.ChatHist;

@Repository
public interface ChatHistRepo extends JpaRepository<ChatHist, Long> {

	// チャット：古い順で全件（文脈構築）
	List<ChatHist> findByUserIdOrderByCreatedAtAsc(String userId);

	// リマインド：新しい順で10件（直近の会話をリマインドに反映）
	List<ChatHist> findTop10ByUserIdOrderByCreatedAtDesc(String userId);

	// チャット文脈用：直近２０件（新しい順）
	List<ChatHist> findTop20ByUserIdOrderByCreatedAtDesc(String userId);

}
