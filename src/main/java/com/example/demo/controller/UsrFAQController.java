package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.FAQService;
import com.example.demo.util.Ut;
import com.example.demo.vo.FAQ;
import com.example.demo.vo.ResultData;
import com.example.demo.vo.Rq;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UsrFAQController {

	@Autowired
	private Rq rq;

	@Autowired
	private FAQService faqService;

	@RequestMapping("/usr/FAQ/detail")
	public String showDetail(HttpServletRequest req, Model model, int id) {

		Rq rq = (Rq) req.getAttribute("rq");

		FAQ faq= faqService.getForPrintFAQ(rq.getLoginedMemberId(), id);

		model.addAttribute("faq", faq);

		return "usr/FAQ/detail";
	}

	@RequestMapping("/usr/FAQ/modify")
	public String showModify(HttpServletRequest req, Model model, int id) {

		Rq rq = (Rq) req.getAttribute("rq");

		FAQ faq = faqService.getForPrintFAQ(rq.getLoginedMemberId(), id);

		if (faq == null) {
			return Ut.jsHistoryBack("F-1", Ut.f("%d번 FAQ는 없습니다", id));
		}

		model.addAttribute("faq", faq);

		return "/usr/FAQ/modify";
	}

	// 로그인 체크 -> 유무 체크 -> 권한 체크 -> 수정
	@RequestMapping("/usr/FAQ/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest req, int id, String title, String body) {

		Rq rq = (Rq) req.getAttribute("rq");

		FAQ faq = faqService.getFAQById(id);

		if (faq == null) {
			return Ut.jsHistoryBack("F-1", Ut.f("%d번 FAQ는 없습니다", id));
		}

		ResultData userCanModifyRd = faqService.userCanModify(rq.getLoginedMemberId(), faq);

		if (userCanModifyRd.isFail()) {
			return Ut.jsHistoryBack(userCanModifyRd.getResultCode(), userCanModifyRd.getMsg());
		}

		if (userCanModifyRd.isSuccess()) {
			faqService.modifyFAQ(id, title, body);
		}

		faq = faqService.getFAQById(id);

		return Ut.jsReplace(userCanModifyRd.getResultCode(), userCanModifyRd.getMsg(), "../FAQ/detail?id=" + id);
	}

	@RequestMapping("/usr/FAQ/doDelete")
	@ResponseBody
	public String doDelete(HttpServletRequest req, int id) {

		Rq rq = (Rq) req.getAttribute("rq");

		FAQ faq = faqService.getFAQById(id);

		if (faq == null) {
			return Ut.jsHistoryBack("F-1", Ut.f("%d번 FAQ는 없습니다", id));
		}

		ResultData userCanDeleteRd = faqService.userCanDelete(rq.getLoginedMemberId(), faq);

		if (userCanDeleteRd.isFail()) {
			return Ut.jsHistoryBack(userCanDeleteRd.getResultCode(), userCanDeleteRd.getMsg());
		}

		if (userCanDeleteRd.isSuccess()) {
			faqService.deleteFAQ(id);
		}

		return Ut.jsReplace(userCanDeleteRd.getResultCode(), userCanDeleteRd.getMsg(), "../FAQ/list");
	}

	@RequestMapping("/usr/FAQ/write")
	public String showWrite(HttpServletRequest req) {

		return "usr/FAQ/write";
	}

	@RequestMapping("/usr/FAQ/doWrite")
	@ResponseBody
	public String doWrite(HttpServletRequest req, String title, String body) {

		Rq rq = (Rq) req.getAttribute("rq");

		if (Ut.isEmptyOrNull(title)) {
			return Ut.jsHistoryBack("F-1", "제목을 입력해주세요");
		}
		if (Ut.isEmptyOrNull(body)) {
			return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
		}

		ResultData writeFAQRd = faqService.writeFAQ(rq.getLoginedMemberId(), title, body);

		int id = (int) writeFAQRd.getData1();

		FAQ faq = faqService.getFAQById(id);

		return Ut.jsReplace(writeFAQRd.getResultCode(), writeFAQRd.getMsg(), "../FAQ/detail?id=" + id);

	}

	@RequestMapping("/usr/FAQ/list")
	public String showList(HttpServletRequest req, Model model,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "title,body") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword) throws IOException {

		Rq rq = (Rq) req.getAttribute("rq");

		int FAQsCount = faqService.getFAQsCount(searchKeywordTypeCode, searchKeyword);

		// 한페이지에 글 10개
		// 글 20개 -> 2page
		// 글 25개 -> 3page
		int itemsInAPage = 10;

		int pagesCount = (int) Math.ceil(FAQsCount / (double) itemsInAPage);

		List<FAQ> FAQs = faqService.getForPrintFAQs(itemsInAPage, page, searchKeywordTypeCode,
				searchKeyword);

		model.addAttribute("faqs", FAQs);
		model.addAttribute("FAQsCount", FAQsCount);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("page", page);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);

		return "usr/FAQ/list";
	}
	
}