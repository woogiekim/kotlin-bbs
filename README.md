# Kotlin BBS

## 주요 사용 기술 및 방법

* spring-boot with kotlin
* jpa
* h2 database
* tdd

## 구현목록

* 게시글 등록
* 게시글 목록 조회
* 게시글 조회
* 게시글 수정
* 게시글 삭제
* 댓글 등록
* 댓글 목록 조회
* 댓글 수정
* 댓글 삭제
* 게시글 좋아요
* 게시글 좋아요 취소

> Board -> 게시글 Comment -> 댓글 Like -> 좋아요
> 
> Like는 게시글에 대해서 좋아요를 한것에 대한 정보이고 제가 좋아요 누르면 저의 이름으로 Entity가 저장이 되는거죠
> 
> Board에서는 Comment의 정보를 모르고 있게 (OneToMany 사용 X)
> 
> Board에서는 Like의 정보를 모르고 있게 (OneToMany 사용 X)