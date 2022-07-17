package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.BoardListAction;
import action.BoardWriteProAction;
import vo.ActionForward;


// 서블릿 주소가 xxx.bo 로 끝날 경우 BoardFrontController 클래스로 해당 요청이 전달됨
@WebServlet("*.bo")
public class BoardFrontController extends HttpServlet {
	// GET 방식 or POST 방식에 따른 처리를 별도로 구분하지 않고 공통으로 처리하기 위해
	// doProcess() 메서드를 정의하고 doGet(), doPost() 메서드에서 호출
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("BoardFrontController");
		
		//post 인코딩
		request.setCharacterEncoding("UTF-8");
		
		// 서블릿 주소 추출
		String command = request.getServletPath();
		System.out.println("command : " + command);
		
		// Action 클래스 인스턴스들을 공통으로 관리하는 Action 타입 변수 선언
		Action action = null;
		
		// 포워딩 정보를 관리하는 ActionForward 타입 변수 선언
		ActionForward forward = null;
		
		// --------------------------------------------------------
		// 포워딩 처리
		
		if(command.equals("/BoardWriteForm.bo")) {
			forward = new ActionForward();
			forward.setPath("board/qna_board_write.jsp");
			forward.setRedirect(false);
		} else if(command.equals("/BoardWritePro.bo")) {
			System.out.println("글쓰기 로직");
			// 글쓰기 작업 완료했다고 가정
			// => 글목록 표시를 위한 BoardList.bo 서블릿 주소 요청하여 포워딩
			// => 새로운 요청에 의한 서블릿 주소를 변경해야하므로 Redirect 방식으로 포워딩
//			forward = new ActionForward();
//			forward.setPath("BoardList.bo");
//			forward.setRedirect(true);
			// ------------------------------
			// 글쓰기 비즈니스 로직 수행을 위한 컨트롤러인 Action 클래스로 이동하여
			// Service -> DAO 클래스를 거쳐 비즈니스 로직을 수행한 후
			// 최종적으로 Action 클래스에서 포워딩 정보를 저장한 후 ActionForward 객체를 리턴
			try {
				action = new BoardWriteProAction();
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		} else if(command.equals("/BoardList.bo")) {
			// 글목록 조회 비즈니스 로직 수행을 위한 컨트롤러인 Action 클래스로 이동하여
			// Service -> DAO 클래스 비즈니스 로직을 수행한 후
			// 최종적으로 Action 클래스에서 포워딩 정보를 저장한 후 ActionForward 객체를 리턴
			try {
				action = new BoardListAction();
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		// --------------------------------------------------------
		// 포워딩 공통 처리
		if(forward != null) {
			// redirect vs dispatcher
			if(forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
		
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}







