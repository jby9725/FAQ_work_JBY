package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.FAQRepository;
import com.example.demo.util.Ut;
import com.example.demo.vo.FAQ;
import com.example.demo.vo.ResultData;

@Service
public class FAQService {

	@Autowired
	private FAQRepository faqRepository;

	public FAQService(FAQRepository faqRepository) {
		this.faqRepository = faqRepository;
	}

	public ResultData writeFAQ(int memberId, String title, String body) {
		faqRepository.writeFAQ(memberId, title, body);

		int id = faqRepository.getLastInsertId();

		return ResultData.from("S-1", Ut.f("%d번 글이 등록되었습니다", id), "등록 된 게시글의 id", id);
	}

	public void deleteFAQ(int id) {
		faqRepository.deleteFAQ(id);
	}

	public void modifyFAQ(int id, String title, String body) {
		faqRepository.modifyFAQ(id, title, body);
	}

	public FAQ getForPrintFAQ(int loginedMemberId, int id) {

		FAQ FAQ = faqRepository.getForPrintFAQ(id);

		controlForPrintData(loginedMemberId, FAQ);

		return FAQ;
	}

	public FAQ getFAQById(int id) {

		return faqRepository.getFAQById(id);
	}

	public List<FAQ> getForPrintFAQs(int itemsInAPage, int page, String searchKeywordTypeCode,
			String searchKeyword) {
		
		int limitFrom = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;

		return faqRepository.getForPrintFAQs(limitFrom, limitTake, searchKeywordTypeCode,
				searchKeyword);
	}

	public List<FAQ> getFAQs() {
		return faqRepository.getFAQs();
	}

	private void controlForPrintData(int loginedMemberId, FAQ FAQ) {
		if (FAQ == null) {
			return;
		}
		ResultData userCanModifyRd = userCanModify(loginedMemberId, FAQ);
		FAQ.setUserCanModify(userCanModifyRd.isSuccess());

		ResultData userCanDeleteRd = userCanDelete(loginedMemberId, FAQ);
		FAQ.setUserCanDelete(userCanModifyRd.isSuccess());
	}

	public ResultData userCanDelete(int loginedMemberId, FAQ faq) {
		if (faq.getMemberId() != loginedMemberId) {
			return ResultData.from("F-2", Ut.f("%d번 게시글에 대한 삭제 권한이 없습니다", faq.getId()));
		}
		return ResultData.from("S-1", Ut.f("%d번 게시글을 삭제했습니다", faq.getId()));
	}

	public ResultData userCanModify(int loginedMemberId, FAQ faq) {
		if (faq.getMemberId() != loginedMemberId) {
			return ResultData.from("F-2", Ut.f("%d번 게시글에 대한 수정 권한이 없습니다", faq.getId()));
		}
		return ResultData.from("S-1", Ut.f("%d번 게시글을 수정했습니다", faq.getId()), "수정된 게시글", faq);
	}

	public int getFAQsCount(String searchKeywordTypeCode, String searchKeyword) {
		return faqRepository.getFAQCount(searchKeywordTypeCode, searchKeyword);
	}

}