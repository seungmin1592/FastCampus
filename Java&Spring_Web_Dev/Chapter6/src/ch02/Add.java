package ch02;

@FunctionalInterface  // 함수형 인터페이스라는 의미, 내부에 여러 개의 메서드를 선언 불가
public interface Add {

    public int add(int x, int y);
}
