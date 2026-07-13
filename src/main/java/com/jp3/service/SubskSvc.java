package com.jp3.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jp3.dto.form.SubskForm;
import com.jp3.entity.Subsk;
import com.jp3.repository.SubskRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubskSvc {

	private final SubskRepo subskRepo;

	//追加：Formつくっていれる
	public void subRegi(SubskForm subskForm) {

		if (subskForm.getUpdateDays() == null) {
			//間の日数を出す
			long days = ChronoUnit.DAYS.between(subskForm.getJoinedAt(), subskForm.getUpdateAt());
			subskForm.setUpdateDays((int) days);

		} else if (subskForm.getUpdateAt() == null) {
			//日数を足す
			LocalDate calcDate = subskForm.getJoinedAt().plusDays(subskForm.getUpdateDays());
			subskForm.setUpdateAt(calcDate);
		} else {
			//本当はここで例外をスローしてcontrollerで失敗を出したいんだけど、後回し
			return;
		}

		Subsk sbsk = new Subsk(
				subskForm.getUserId(),
				subskForm.getSubskName(),
				subskForm.getJoinedAt(),
				subskForm.getUpdateAt(),
				subskForm.getUpdateDays());

		subskRepo.save(sbsk);
	}

	//全件表示
	public List<Subsk> getSbskList(String userId) {
		List<Subsk> sbl = subskRepo.findByUserId(userId);
		return sbl;
	}

	//今日が期限のサブスクを探す
	public List<Subsk> getTodaySbskByUser(String userId, LocalDate today) {
		List<Subsk> todaysbl = subskRepo.findByUpdateAtAndUserId(today, userId);
		return todaysbl;
	}

	//サブスクの内容編集
	public void editSbsk(SubskForm subskForm, String userId) {

		if (subskForm.getUpdateDays() == null) {
			//間の日数を出す
			long days = ChronoUnit.DAYS.between(subskForm.getJoinedAt(), subskForm.getUpdateAt());
			subskForm.setUpdateDays((int) days);

		} else if (subskForm.getUpdateAt() == null) {
			//日数を足す
			LocalDate calcDate = subskForm.getJoinedAt().plusDays(subskForm.getUpdateDays());
			subskForm.setUpdateAt(calcDate);
		} else {
			//本当はここで例外をスローしてcontrollerで失敗を出したいんだけど、今は後回し
			return;
		}

		Subsk sbsk = subskRepo.findBySubskIdAndUserId(subskForm.getSubskId(), userId)
				.orElseThrow(() -> new IllegalArgumentException("対象のサブスクが見つかりません"));
		BeanUtils.copyProperties(subskForm, sbsk, "subskId", "userId");

		subskRepo.save(sbsk);
	}

	//削除
	public void delSbsk(List<Long> subskId, String userId) {
		List<Subsk> delsbsk = subskRepo.findBySubskIdInAndUserId(subskId, userId);
		subskRepo.deleteAll(delsbsk);

	}

}
