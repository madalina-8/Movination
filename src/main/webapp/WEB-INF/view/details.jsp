<%--
  Created by IntelliJ IDEA.
  User: crbe1606
  Date: 7/14/2022
  Time: 8:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@include file="/WEB-INF/tags/head.tag" %>
<body>
<n:navbar/>
<div class="container moviedetails" id="moviedetails">
    <div class="animate__animated animate__fadeInDown">
        <div id="mainContainer">
            <div class="movie-cover">
                <img id="poster" src="${movieDetails.picture}" alt="img">
            </div>
            <div id="info">
                <h2>${movieDetails.name}</h2>
                <h5>
                    <c:choose>
                        <c:when test="${movieDetails.rating!=null&&movieDetails.rating>0}">
                            <p> Movie rating: ${movieDetails.rating} <i class='fa-solid fa-star'
                                                                         style='color: #f3da35'></i>
                            </p>
                        </c:when>
                        <c:otherwise>
                            <p> Movie rating: No rating
                            </p>
                        </c:otherwise>
                    </c:choose>
                    <p>Year: ${movieDetails.year}</p>
                    <p>Runtime: ${movieDetails.minutes} min </p>
                    <p> <c:forEach items="${moviecategory}" var="category" varStatus="status">
                        <c:choose>
                            <c:when test="${status.count < fn:length(moviecategory)}">
                                ${category.name} /
                            </c:when>
                            <c:otherwise>
                                ${category.name}
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    </p>
                </h5>
                <p>${movieDetails.description}</p>
                <c:choose>
                    <c:when test="${isInWatchlist==true}">
                        <button class="addlist" id="removeMovie" movie="${movieDetails.id}">Remove from Watchlist
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="addlist" id="addMovie" movie="${movieDetails.id}">Add to Watchlist</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="../../js/watchlist.js"></script>
<n:footer/>
</body>
</html>
