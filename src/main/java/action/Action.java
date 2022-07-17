package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.ActionForward;

public interface Action {
	// 각 요청을 받아들일 execute() 메서드를 추상메서드로 정의
		// => 파라미터 : HttpServletRequest 객체, HttpServletResponse 객체
		// => 리턴타입 : ActionForward 타입(포워딩 정보)
		// => 내부에서 발생하는 모든 예외를 외부로 위임(던짐)
		public abstract ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
