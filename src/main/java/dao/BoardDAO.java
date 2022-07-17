package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vo.BoardDTO;
import static db.JdbcUtil.*;

public class BoardDAO {
	// ---------------- 싱글톤 디자인 패턴을 활용한 BoardDAO 인스턴스 생성 작업 ---------------
		// 1. 외부에서 인스턴스 생성이 불가능하도록 생성자 정의 시 private 접근제한자 적용
//		private BoardDAO() {}
		
		// 2. 자신의 클래스 내에서 직접 인스턴스 생성하여 변수(instance)에 저장
		// => 외부에서 멤버변수에 접근이 불가능하도록 private 접근제한자 적용
		// => 클래스 로딩 시 getInstance() 메서드와 함께 로딩되기 위해 static 멤버변수로 선언
//		private static BoardDAO instance = new BoardDAO();

		// 3. 생성된 인스턴스를 외부로 리턴하기 위한 Getter 정의
		// => 외부에서 인스턴스 생성 없이도 호출이 가능하도록 static 메서드로 정의
		// => 이 때, 2번에서 선언된 멤버변수(instance)도 static 변수로 선언되어야 함
		//    (static 메서드 내에서 접근하는 변수도 static 변수여야하기 때문에)
//		public static BoardDAO getInstance() {
//			return instance;
//		}
		// ----------------------------------------------------------------------------------------
		// 1. 멤버변수 선언 및 인스턴스 생성
		private static BoardDAO instance = new BoardDAO();
		// 2. 생성자 정의
		private BoardDAO() {}
		// 3. Getter 정의(자동 생성)
		public static BoardDAO getInstance() {
			return instance;
		}
		// ----------------------------------------------------------------------------------------
		// 외부(Service 클래스)로부터 Connection 객체를 전달받아 관리하기 위해
		// Connection 타입 멤버변수와 Setter 메서드 정의
		private Connection con;
		public void setConnection(Connection con) {
			this.con = con;
		}
		// ----------------------------------------------------------------------------------------
		// 글쓰기
		// 글쓰기 작업을 수행할 insertBoard() 메서드 정의
		// => 파라미터 : BoardDTO 객체(board)   리턴타입 : int(insertCount)
		public int insertBoard(BoardDTO board) {
//			System.out.println("BoardDAO - insertBoard()");
			
			// INSERT 작업 결과를 리턴받아 저장할 변수 선언
			int insertCount = 0;
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			int num = 1; // 새 글 번호를 저장할 변수
			
			try {
				// 새 글 번호로 사용될 번호를 생성하기 위해 기존 게시물의 가장 큰 번호 조회
				// => 조회 결과가 있을 경우 해당 번호 + 1 값을 새 글 번호로 저장
				String sql = "SELECT MAX(board_num) FROM board";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					num = rs.getInt(1) + 1; // 조회된 가장 큰 번호 + 1 값을 새 글 번호로 저장
				}
				
				// 사용 완료된 PreparedStatement 객체를 먼저 반환
				close(pstmt);
				
				// 전달받은 데이터를 board 테이블에 INSERT
				sql = "INSERT INTO board VALUES (?,?,?,?,?,?,?,?,?,?,?,now())";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, num);
				pstmt.setString(2, board.getBoard_name());
				pstmt.setString(3, board.getBoard_pass());
				pstmt.setString(4, board.getBoard_subject());
				pstmt.setString(5, board.getBoard_content());
				pstmt.setString(6, board.getBoard_file());
				pstmt.setString(7, board.getBoard_real_file());
				// 답글에 사용될 참조글 번호(board_re_ref)는 새 글에 대한 번호인 새 글 번호와 동일하게 지정
				pstmt.setInt(8, num); // board_re_ref
				pstmt.setInt(9, 0); // board_re_lev
				pstmt.setInt(10, 0); // board_re_seq
				pstmt.setInt(11, 0); // board_readcount
				System.out.println(board);
				
				insertCount = pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("SQL 구문 오류 발생! - insertBoard()");
			} finally {
				// DB 자원 반환(주의! Connection 객체는 반환 금지! => Service 클래스에서 반환)
				close(rs);
				close(pstmt);
			}
			
			return insertCount;
		}
		}


