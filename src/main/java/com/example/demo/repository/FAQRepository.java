package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.FAQ;

@Mapper
public interface FAQRepository {
	@Insert("""
			INSERT INTO FAQ
			SET regDate = NOW(),
			updateDate = NOW(),
			memberId = #{memberId},
			title = #{title},
			`body` = #{body}
			""")
	public void writeFAQ(int memberId, String title, String body);

	
	@Delete("""
			DELETE FROM FAQ
			WHERE id = #{id}
			""")
	public void deleteFAQ(int id);

	@Update("""
			UPDATE FAQ
			SET updateDate = NOW(),
			title = #{title},
			`body` = #{body}
			WHERE id = #{id}
			""")
	public void modifyFAQ(int id, String title, String body);

	@Select("""
			SELECT F.* , M.nickname AS extra__writer
			FROM FAQ AS F
			INNER JOIN `member` AS M
			ON F.memberId = M.id
			WHERE F.id = #{id}
				""")
	public FAQ getForPrintFAQ(int id);
	
	@Select("""
			SELECT *
			FROM FAQ
			WHERE id = #{id}
				""")
	public FAQ getFAQById(int id);

	@Select("""
			<script>
				SELECT F.* , M.nickname AS extra__writer
				FROM FAQ AS F
				INNER JOIN `member` AS M
				ON F.memberId = M.id
				WHERE 1
				<if test="searchKeyword != ''">
					<choose>
						<when test="searchKeywordTypeCode == 'title'">
							AND F.title LIKE CONCAT('%', #{searchKeyword}, '%')
						</when>
						<when test="searchKeywordTypeCode == 'body'">
							AND F.`body` LIKE CONCAT('%', #{searchKeyword}, '%')
						</when>
						<when test="searchKeywordTypeCode == 'nickname'">
							AND M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
						</when>
						<otherwise>
							AND F.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR F.`body` LIKE CONCAT('%', #{searchKeyword}, '%')
						</otherwise>
					</choose>
				</if>
				ORDER BY F.id DESC
				<if test="limitFrom >= 0">
					LIMIT #{limitFrom}, #{limitTake}
				</if>
				</script>
			""")
	public List<FAQ> getForPrintFAQs(int limitFrom, int limitTake, String searchKeywordTypeCode, String searchKeyword);
	
	@Select("""
			SELECT F.* , M.nickname AS extra__writer
			FROM FAQ AS F
			INNER JOIN `member` AS M
			ON F.memberId = M.id
			ORDER BY F.id DESC
			""")
	public List<FAQ> getFAQs();

	@Select("SELECT LAST_INSERT_ID();")
	public int getLastInsertId();

	@Select("""
			<script>
				SELECT COUNT(*) , M.nickname AS extra__writer
				FROM FAQ AS F
				INNER JOIN `member` AS M
				ON F.memberId = M.id
				WHERE 1
				<if test="searchKeyword != ''">
					<choose>
						<when test="searchKeywordTypeCode == 'title'">
							AND F.title LIKE CONCAT('%', #{searchKeyword}, '%')
						</when>
						<when test="searchKeywordTypeCode == 'body'">
							AND F.`body` LIKE CONCAT('%', #{searchKeyword}, '%')
						</when>
						<when test="searchKeywordTypeCode == 'nickname'">
							AND M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
						</when>
						<otherwise>
							AND F.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR F.`body` LIKE CONCAT('%', #{searchKeyword}, '%')
						</otherwise>
					</choose>
				</if>
				ORDER BY F.id DESC;
			</script>
			""")
	public int getFAQCount(String searchKeywordTypeCode, String searchKeyword);
	
}