<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%String screenFlg = (String)request.getAttribute("screenFlg");%>
<%String lineStr = (String)request.getAttribute("lineStr");%>
<html>
<head>
<jsp:include page="common.jsp" flush="true" />
<title>適正テスト１</title>
</head>
<body>
    <script>
    var alertmsg = function(){
  	alert("時間です！\n次のページに進みます。\n練習問題の正解はそれぞれ①と②です。");
  	var test1 = document.getElementById("test1_info");
  	var input = document.createElement('input');
  	input.setAttribute('name', 'action');
  	input.setAttribute('value', 'GoToTest1');
  	test1.appendChild(input);
  	test1.submit();
	}
	setTimeout(alertmsg, 180000);
	</script>
<div class="container">

<h1>テスト１ 説明</h1>
<h2>所要時間 ３分</h2>

<font color="red">※３分後に自動的にテストページに進みます。</font>

<h6>問</h6>
下記をよく読み、解答してください。<br>
次ページ以降には、一文を分割した言葉が５つ並んでいます。<br>
正しく並び替えた上で、指定の順番に該当する選択肢を選んでください。<br>
<br>
<h6>例</h6>
（１）並び替えたときに４番目になる選択肢を解答してください。<br>
①把握できる ②経営者が社内外の ③かつ的確に ④ような仕組みが必要だ ⑤情報を迅速<br>
<br>
<h6>答</h6>
経営者が社内外の／情報を迅速／かつ的確に／把握できる／ような仕組みが必要だ<br>
４番目は「把握できる」であり、よって正解は選択肢①です。<br>
<form id="test1_info" action="ActionServlet" method="post">
<br>
<h6>練習問題</h6>
（１）並び替えたときに３番目になる選択肢を解答してください。<br>
①重視してアルバイトを ②時給の高さだけでなく ③選ぶ人が ④増えている ⑤働きやすさを<br>
<input type="radio" name="answer1" value="1">（１）<br>
<input type="radio" name="answer1" value="2">（２）<br>
<input type="radio" name="answer1" value="3">（３）<br>
<input type="radio" name="answer1" value="4">（４）<br>
<input type="radio" name="answer1" value="5">（５）<br>
<br>

（２）並び替えたときに５番目になる選択肢を解答してください。<br>
①だけでなく ②好感がもてる ③身だしなみがよい ④礼儀正しいので ⑤この店の店員は<br>
<input type="radio" name="answer2" value="1">（１）<br>
<input type="radio" name="answer2" value="2">（２）<br>
<input type="radio" name="answer2" value="3">（３）<br>
<input type="radio" name="answer2" value="4">（４）<br>
<input type="radio" name="answer2" value="5">（５）<br>
<br>
<table>
<tr>
<td>
<label><input type="checkbox" name="answer" value="hoge1" onclick="entryChange1(this.checked);" />練習問題正解を表示</label>
</td>
</tr>
</table>

<!-- 表示非表示切り替え -->
<div id="firstNotice">
正解はそれぞれ①と②です。
</div>
<br>
<input type="hidden" name="screenFlg" value=<%=screenFlg%>>
<input type="hidden" name="lineStr" value=<%=lineStr%>>
<button type="submit" class="btn btn-success" name="action" value="GoToTest1">テスト１本題へ</button>
</form>
</div>
<br>
</body>
</html>