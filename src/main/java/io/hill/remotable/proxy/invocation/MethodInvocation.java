package io.hill.remotable.proxy.invocation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MethodInvocation {

	private String method;
	private Object[] arguments;
}
