package com.side.domain.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.side.global.util.PageNavigation;

import lombok.RequiredArgsConstructor;

import com.side.domain.dto.BoardDto;
import com.side.domain.dto.BoardParameterDto;
import com.side.domain.mapper.BoardMapper;
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {
	
	private final SqlSession sqlSession;

	@Override
	public boolean writeArticle(BoardDto boardDto) throws Exception {
		if(boardDto.getSubject() == null || boardDto.getContent() == null) {
			throw new Exception();
		}
		return sqlSession.getMapper(BoardMapper.class).writeArticle(boardDto) == 1;
	}

	@Override
	public List<BoardDto> listArticle(BoardParameterDto boardParameterDto) throws Exception {
		int start = boardParameterDto.getPg() == 0 ? 0 : (boardParameterDto.getPg() - 1) * boardParameterDto.getSpp();
		boardParameterDto.setStart(start);
		return sqlSession.getMapper(BoardMapper.class).listArticle(boardParameterDto);
	}

//	@Override
//	public PageNavigation makePageNavigation(BoardParameterDto boardParameterDto) throws Exception {
//		int naviSize = 5;
//		PageNavigation pageNavigation = new PageNavigation();
//		pageNavigation.setCurrentPage(boardParameterDto.getPg());
//		pageNavigation.setNaviSize(naviSize);
//		int totalCount = sqlSession.getMapper(BoardMapper.class).getTotalCount(boardParameterDto);//총글갯수  269
//		pageNavigation.setTotalCount(totalCount);  
//		int totalPageCount = (totalCount - 1) / boardParameterDto.getSpp() + 1;//27
//		pageNavigation.setTotalPageCount(totalPageCount);
//		boolean startRange = boardParameterDto.getPg() <= naviSize;
//		pageNavigation.setStartRange(startRange);
//		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < boardParameterDto.getPg();
//		pageNavigation.setEndRange(endRange);
//		pageNavigation.makeNavigator();
//		return pageNavigation;
//	}

	@Override
	public BoardDto getArticle(int articleNo) throws Exception {
		return sqlSession.getMapper(BoardMapper.class).getArticle(articleNo);
	}
	
	@Override
	public void updateHit(int articleNo) throws Exception {
		sqlSession.getMapper(BoardMapper.class).updateHit(articleNo);
	}

	@Override
	@Transactional
	public boolean modifyArticle(BoardDto boardDto) throws Exception {
		return sqlSession.getMapper(BoardMapper.class).modifyArticle(boardDto) == 1;
	}

	@Override
	@Transactional
	public boolean deleteArticle(int articleno) throws Exception {
		return sqlSession.getMapper(BoardMapper.class).deleteArticle(articleno) == 1;
	}
}