package com.neodain.springbootbatchdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

// This configuration class defines a transaction manager bean for managing transactions in a Spring Boot application.
// The @Configuration annotation indicates that this class can be used by the Spring IoC container as a source of bean definitions.

@Configuration
public class TransactionManagerConfig {

  @Bean // The @Bean annotation indicates that this method will return an object that should be registered as a bean in the Spring application context.
  // The method returns a PlatformTransactionManager, which is responsible for managing transactions in a Spring application.
  // The method takes an EntityManagerFactory as a parameter, which is used to create the transaction manager.
  // The EntityManagerFactory is typically configured elsewhere in the application, such as in a JPA configuration class.

  @Primary 
  // @Primary annotation is used to indicate that this bean should be preferred when multiple beans of the same type are present.
  // This is useful when you have multiple transaction managers in your application, and you want to specify which one should be used by default.
  // In this case, it indicates that this JpaTransactionManager should be the primary transaction manager used by the application.
  // The JpaTransactionManager is a specific implementation of PlatformTransactionManager that is designed to work with JPA (Java Persistence API).
  // It provides support for managing transactions in a JPA-based application, allowing you to perform operations such as committing or rolling back transactions.
  // This is particularly useful in applications that use JPA for data access, as it ensures that transactions are handled correctly when interacting with the database.
  // The JpaTransactionManager will use the provided EntityManagerFactory to create and manage transactions.
  
  /*
  1. 오류 메시지 : 
    - NoUniqueBeanDefinitionException: 
    No qualifying bean of type 'org.springframework.transaction.PlatformTransactionManager' available: 
    expected single matching bean but found 2: transactionManager,springCloudTaskTransactionManager
  
  2. 오류 원인 : 
    - Spring이 TransactionManager 빈을 주입하려고 시도했지만, 
    transactionManager와 springCloudTaskTransactionManager라는 두 개의 빈이 정의되어 있어 어떤 것을 사용할지 결정하지 못했습니다.
    이로 인해 NoUniqueBeanDefinitionException이 발생했습니다.

    - 다중 TransactionManager 빈 정의
    Spring Boot 프로젝트에서 spring-cloud-task를 사용하고 있는 경우, 기본적으로 springCloudTaskTransactionManager라는 
    추가적인 TransactionManager 빈이 생성됩니다.
    동시에, JPA 또는 다른 데이터베이스 설정에서 transactionManager라는 기본 TransactionManager 빈이 생성됩니다.
    이로 인해 Spring은 어떤 TransactionManager를 사용할지 명확히 지정하지 않으면 충돌이 발생합니다.

  3. 해결 방법
    - @Primary 애노테이션을 사용하여 기본 TransactionManager를 명시적으로 지정합니다.
    - 이 애노테이션은 Spring이 다중 빈 중에서 어떤 빈을 우선적으로 사용할지 결정하는 데 도움을 줍니다.
    - 예를 들어, @Primary가 지정된 transactionManager 빈이 우선적으로 사용됩니다

    - @Transactional 사용 시 기본 TransactionManager 지정 누락
    Spring은 어떤 TransactionManager를 사용할지 명확히 지정하지 않으면 충돌이 발생할 수 있습니다.
    """
    @Service
    @Transactional(transactionManager = "transactionManager") // 사용할 TransactionManager 지정
    public class DevopsServiceImpl implements IDevopsService {
      // 서비스 로직...
    } 
    """

    - 테스트 환경에서도 동일한 문제가 발생할 수 있습니다.
    - 테스트 클래스에서 @Transactional 애노테이션을 사용하여 TransactionManager를 명시적으로 지정하지 않으면
    """
    @SpringBootTest
    @TestPropertySource(properties = "spring.cloud.task.enabled=false") // 테스트에서 spring-cloud-task 비활성화
      public class DevopsServiceImplTest {
      // 테스트 코드...
    }
    """

    - application.yml에 spring.cloud.task.enabled=false 추가
    spring.cloud.task.enabled=false 설정은 Spring Cloud Task 기능을 비활성화합니다.
    이 설정을 추가하면 springCloudTaskTransactionManager 빈이 생성되지 않으므로, 
    TransactionManager 빈 충돌 문제가 해결될 가능성이 높습니다.
    application.yml 파일에 다음과 같이 추가할 수 있습니다:
    """
    spring:
      cloud:
        task:
          enabled: false
    ```
   */

  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
