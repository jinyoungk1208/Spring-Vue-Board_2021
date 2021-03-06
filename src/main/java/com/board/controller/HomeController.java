package com.board.controller;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.board.dto.FileDto;
import com.board.dto.HomeDto;
import com.board.dto.MemberDto;
import com.board.service.HomeService;

@Controller
public class HomeController {
	
	@Inject
	private HomeService homeService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getPaging(Model model, HttpServletRequest req) throws Exception {
		String sortOption = "";
		if(req.getParameter("sortOption") != null) {
			sortOption = req.getParameter("sortOption");
		}
		
		String searchOption = "";
		if(req.getParameter("searchOption") != null) {
			searchOption = req.getParameter("searchOption");
		}
		String searchTxt = "";
		if(req.getParameter("search") != null) {
			searchTxt = req.getParameter("search");
		}
		System.out.println(searchTxt);
		
		int num = ( req.getParameter("num") != null ) ? Integer.parseInt(req.getParameter("num")) : 1;
		// 게시물 갯수
		int count = homeService.count(searchOption, searchTxt);
		// 한 페이지 당 게시물 갯수
		int postNum = 10;
		// 페이지 갯수
		int pageNum = (int)Math.ceil((double)count / postNum);
		// 출력할 게시물
		int displayPost = (num - 1) * postNum;
		// 한번에 표시할 페이지 갯수
		int pageNum_cnt = 10;
		// 표시되는 페이지 번호 중 마지막 번호
		int endPageNum = (int)(Math.ceil((double)num / (double)pageNum_cnt) * pageNum_cnt);
		// 표시되는 페이지 번호 중 첫번째 번호
		int startPageNum = endPageNum - (pageNum_cnt - 1);
		// 마지막 번호 재계산
		int endPageNum_tmp = (int)(Math.ceil((double)count / (double)pageNum_cnt));
		
		if (endPageNum > endPageNum_tmp) {
			endPageNum = endPageNum_tmp;
		}
		
		boolean prev = startPageNum == 1 ? false : true;
		boolean next = endPageNum * pageNum_cnt >= count ? false : true;
		
		List<HomeDto> list = null;
		list = homeService.paging(postNum, displayPost, sortOption, searchOption, searchTxt);
		
		HttpSession httpSession = req.getSession(true);
		MemberDto memberDto = (MemberDto)httpSession.getAttribute("member");
		
		boolean result = false;
		
		if(memberDto != null) {
			System.out.println(memberDto.getmId());
			System.out.println(memberDto.getmName());
			memberDto.getmId();
			memberDto.getmName();
			result = true;
		}
		
		try {
			
			model.addAttribute("sortOption",sortOption);
			model.addAttribute("searchOption",searchOption);
			model.addAttribute("search",searchTxt);
			
			model.addAttribute("list", list);
			model.addAttribute("pageNum", pageNum);
			
			// 시작 및 끝 번호
			model.addAttribute("startPageNum", startPageNum);
			model.addAttribute("endPageNum", endPageNum);
			
			// 이전 및 다음
			model.addAttribute("prev", prev);
			model.addAttribute("next", next);
			
			// 현재 페이지
			model.addAttribute("select", num);

			// 로그인 체크
			model.addAttribute("result", result);
		} catch ( Exception e ) {}
		
		return "home";
	}
	
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public void getWrite() throws Exception {
		
	}
	
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String postWrite(FileDto fileDto, MultipartHttpServletRequest mreq, HttpServletRequest req) throws Exception {
		HttpSession httpSession = req.getSession(true);
		MemberDto memberDto = (MemberDto)httpSession.getAttribute("member");
		fileDto.setWriter(memberDto.getmId());
		
		homeService.write(fileDto, mreq);
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String getDetail(Model model, int bno) throws Exception {
		FileDto data = homeService.detail(bno);
		
		try {
			model.addAttribute("detail", data);
		} catch (Exception e) {}

		return "detail";
	}
	
	@RequestMapping(value = "/detailContent", method = RequestMethod.GET)
	@ResponseBody
	public FileDto getDetailContent(@RequestParam(value="bno") String bno, HttpServletRequest req) throws Exception {
		HttpSession httpSession = req.getSession(true);
		MemberDto memberDto = (MemberDto)httpSession.getAttribute("member");
		
		FileDto data = homeService.detail(Integer.parseInt(bno));
		
		boolean resultBool = false;
		
		if(memberDto == null) {
			data.setResult(resultBool);
			return data;
		} else if (memberDto.getmId().equals(data.getWriter())) {
			resultBool = true;
			data.setResult(resultBool);
		}
		
		return data;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String getUpdate(@RequestParam(value = "bno") String bno, Model model) throws Exception {
		FileDto data = homeService.detail(Integer.parseInt(bno));
		
		try {
			model.addAttribute("detail", data);
		} catch ( Exception e ) {}
		
		return "update";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String postUpdate(HomeDto homeDto) throws Exception {
		homeService.update(homeDto);
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String getDelete(Model model, int bno) throws Exception {
		homeService.delete(bno);
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/fileDownload", method = RequestMethod.GET)
	public String postFileDownload(HttpServletRequest req, HttpServletResponse res, int bno) throws Exception {
		FileDto data = homeService.fileDownload(bno);
		
		System.out.println(data.getOrg_fname());
		System.out.println(data.getSave_fname());
		System.out.println(data.getFpath());
		
		String save_fname = data.getSave_fname();
		String org_fname = data.getOrg_fname();
		String fpath = data.getFpath();
		
		res.setHeader("Content-Disposition", "attachment; filename=\""+org_fname+"\"");
		res.setHeader("Content-Transfer-Encoding", "binary");
		res.setHeader("Content-Type", "application/octet-stream");
		res.setHeader("Pragma", "no-cache;");
		res.setHeader("Expires", "-1");
		
		OutputStream os = res.getOutputStream();
		FileInputStream fis = new FileInputStream(fpath);
		
		int readCount = 0;
		byte[] buffer = new byte[1024];
		
		while ((readCount = fis.read(buffer)) != -1) {
			os.write(buffer, 0, readCount);
		}
		fis.close();
		os.close();
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/viewCount", method = RequestMethod.GET)
	@ResponseBody
	public void viewCount(@RequestParam(value="bno") String _bno, HttpServletRequest req, HttpServletResponse res) throws Exception {
		int bno = Integer.parseInt(_bno);
		int viewCnt = 0;
		
		Cookie[] cookies = req.getCookies();
		
		if (cookies != null) {
			for (int i=0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("bno" + bno)) {
					viewCnt = 0;
					break;
				} else {
					Cookie cookie = new Cookie("bno" + bno, _bno);
					System.out.println(cookie);
					cookie.setMaxAge(60*60*24);
					res.addCookie(cookie);
					
					viewCnt += 1;
				}
			} 
		}
		
		if (viewCnt > 0) {
			homeService.viewCount(bno);			
		}
	}
	
}
