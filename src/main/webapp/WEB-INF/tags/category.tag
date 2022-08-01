<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="category-navbar">
    <c:forEach items="${categories}" var="category">
        <span class="categories" value="${category.name}" id="${category.id}"><a href=" ">${category.name}</a></span>
    </c:forEach>
</div>