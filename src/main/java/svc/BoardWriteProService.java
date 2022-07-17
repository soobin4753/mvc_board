package svc;

import java.sql.Connection;

import dao.BoardDAO;
import vo.BoardDTO;
import static db.JdbcUtil.*;

public class BoardWriteProService {

	public boolean registBoard(BoardDTO board) {
		System.out.println("BoardWriteProService - registBoard()");
		
		// 1. 글쓰기 작업 요청 처리 결과를 판별하여 리턴하기 위한 boolean 타입 변수 선언
		boolean isWriteSuccess = false;
		
		// 2.JdbcUtil 클래스로부터 Connection Pool 에 저장된 Connection 객체 가져오기 - 공통
		// => Service 클래스에서 트랜잭션 관리를 위해 Connection 객체에 접근해야하기 때문에
		Connection con = getConnection();
		
		// 3. BoardDAO 클래스로부터 BoardDAO 인스턴스 가져오기 - 공통
		// => 싱글톤 디자인 패턴으로 생성된 BoardDAO 인스턴스 가져오기
		BoardDAO dao = BoardDAO.getInstance();
		
		// 4. BoardDAO 인스턴스에 Connection 객체 전달하기 - 공통
		dao.setConnection(con);
				
		// 5. BoardDAO 인스턴스의 XXX 메서드 호출하여 요청받은 XXX 작업 수행 및 결과 리턴받기
		// BoardDAO 객체의 insertBoard() 메서드를 호출하여 글쓰기 작업 수행 후 결과 리턴받기
		// => 파라미터 : BoardDTO 객체(board)   리턴타입 : int(insertCount)
		int insertCount = dao.insertBoard(board);
		
		// 6. 리턴받은 작업 수행 결과를 통해 판별 후 트랜잭션 처리 작업 수행
		if(insertCount > 0) { // 작업 성공 시
		// 트랜잭션 적용을 위해 JdbcUtil 클래스의 commit() 메서드를 호출하여 commit 작업 수행
			commit(con);
					
		// 작업 처리 결과를 성공으로 표시하기 위해 isWriteSuccess 를 true 로 변경
			isWriteSuccess = true;
		} else { // 작업 실패 시
			// 트랜잭션 취소를 위해 JdbcUtil 클래스의 rollback() 메서드를 호출하여 rollback 작업 수행
			rollback(con);
		}
				
		// 7. Connection 객체 반환 - 공통
		close(con);
		
		return isWriteSuccess;
	}

}
