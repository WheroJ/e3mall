<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>student</title>
	</head>
	<body>
		ID: ${stu.id} <br />
		姓名：${stu.name } <br />
		年龄: ${stu.age } <br />
		生日: ${stu.birthday?datetime} <br />
		生日: ${stu.birthday?date} <br />
		生日: ${stu.birthday?string("yyyy/MM/dd")} <br />
		爱好: ${stu.hobby!"没有爱好" } <br />
		爱好:
		<#if stu.hobby??>
			<!--当不为null时 -->
			${stu.hobby! }
		<#else>
			没有爱好
		</#if> 
		<table bgcolor="#83B77B" cellpadding="0" cellspacing="0">
			<tr>
				<th>索引</th>
				<th>ID</th>
				<th>姓名</th>
				<th>爱好</th>
			</tr>
			<#list stuList as stu>
				<tr>
					<td>${stu_index}</td>
					<td>${stu.id}</td>
					<td>${stu.name}</td>
					<td>${stu.hobby!"没有爱好"}</td>
				</tr>
			</#list>
		</table>
		
		<#include "hello.ftl">	
	</body>
</html>
