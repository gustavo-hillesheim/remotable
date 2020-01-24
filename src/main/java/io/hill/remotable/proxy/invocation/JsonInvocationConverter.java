package io.hill.remotable.proxy.invocation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonInvocationConverter extends RemoteInvocationConverter<String> {

	private Gson gson = new GsonBuilder().create();

	@Override
	public String fromMethodInvocation(MethodInvocation methodInvocation) {
		return gson.toJson(methodInvocation);
	}

	@Override
	public MethodInvocation toMethodInvocation(String source) {
		return gson.fromJson(source, MethodInvocation.class);
	}
}
