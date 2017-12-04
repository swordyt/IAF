package com.swordyt.core;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("classpath:qacontext/applicationContext.xml")
@Transactional
// @Transactional(propagation = Propagation.REQUIRED)
// AbstractTransactionalTestNGSpringContextTests
public class BaseTestCase extends AbstractTransactionalTestNGSpringContextTests {
}
