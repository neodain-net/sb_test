DROP TABLE IF EXISTS DEVOPS_MEMBER;
CREATE TABLE DEVOPS_MEMBER (
    MEMBER_ID VARCHAR(36) PRIMARY KEY,
    NAME VARCHAR(50) NOT NULL ,
    -- GENDER ENUM('male', 'female') NOT NULL,
    GENDER VARCHAR(10) NOT NULL,
    BIRTH_DAY DATE,
    PHONE_NUM VARCHAR(20) NOT NULL UNIQUE,
    EMAIL VARCHAR(50) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS DEVOPS;
CREATE TABLE DEVOPS (
    DEVOPS_ID VARCHAR(36) NOT NULL PRIMARY KEY,
    NAME VARCHAR(50) NOT NULL,
    INTRO VARCHAR(100) NOT NULL,
    FOUNDATION_TIME DATETIME
);

DROP TABLE IF EXISTS DEVOPS_MEMBERSHIP;
CREATE TABLE DEVOPS_MEMBERSHIP (
    /* 복합 키 대신 surrogate key 사용 : 확장성과 Join 편의성 증가 */
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    DEVOPS_ID VARCHAR(36) NOT NULL,
    MEMBER_ID VARCHAR(36) NOT NULL,
    -- ROLE_IN_DEVOPS ENUM('beginner', 'maintainer', 'manager', 'leader') NOT NULL DEFAULT 'beginner',
    ROLE VARCHAR(20) NOT NULL DEFAULT 'beginner',
    /*
    H2는 ENUM 타입과 MySQL의 UNIQUE KEY 구문을 지원하지 않는다
    테스트용 스키마에서 ENUM -> VARCHAR, UNIQUE KEY -> UNIQUE로 변경 필요.
    또는 JPA의 자동 DDL 생성을 활용.
    */
    JOIN_DATE DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- UNIQUE KEY UNIQ_MEMBERSHIP (CLUB_ID, MEMBER_ID),
    UNIQUE (DEVOPS_ID, MEMBER_ID),
    FOREIGN KEY (DEVOPS_ID) REFERENCES DEVOPS(DEVOPS_ID),
    FOREIGN KEY (MEMBER_ID) REFERENCES DEVOPS_MEMBER(MEMBER_ID)
    /* 복합 기본키 사용의 경우 : DEVOPS_MEMBERSHIP은 복합 기본키 (CLUB_ID, MEMBER_ID) 로 구성해야
       각 클럽마다 멤버가 1회만 가입 가능하면서도 여러 클럽에 가입 가능하게 된다 (단순하고 명확한 N:N 관계 표현).
    DEVOPS_ID VARCHAR(36) NOT NULL,
    MEMBER_ID VARCHAR(36) NOT NULL,
    ROLE_IN_DEVOPS ENUM('beginner', 'maintainer', 'manager', 'leader') NOT NULL DEFAULT 'beginner',
    JOIN_DATE DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (DEVOPS_ID, MEMBER_ID), -- 복합 기본키 사용
    FOREIGN KEY (DEVOPS_ID) REFERENCES DEVOPS_CLUB(DEVOPS_ID),
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER(MEMBER_ID)
     */
);

DROP TABLE IF EXISTS ADDRESS;   -- (ADDRESS -> CITY -> STATE -> COUNTRY)
CREATE TABLE ADDRESS (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MEMBER_ID VARCHAR(36) NOT NULL,
    -- ADDRESS_TYPE ENUM('home', 'work', 'billing', 'shipping') NOT NULL,
    STATE VARCHAR(50) NOT NULL,
    CITY VARCHAR(20) NOT NULL,
    STREET VARCHAR(50) NOT NULL,
    ADDRESS_LINE VARCHAR(100), -- (optional)
    ZIP_CODE VARCHAR(20),
    FOREIGN KEY (MEMBER_ID) REFERENCES DEVOPS_MEMBER(MEMBER_ID) ON DELETE CASCADE -- Member 삭제 시 관련 주소도 삭제 됨
)
