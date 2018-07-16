public class GenericArrayStack<E> implements Stack<E> {
   
   private int init;
   private E[] elems;
   private int top;
   private final String TAG = GenericArrayStack.class.getSimpleName();

   // Constructor
    public GenericArrayStack( int capacity ) {
        elems = (E[]) new Object[capacity];
        top = 0;
    }

    // Returns true if this ArrayStack is empty
    public boolean isEmpty() {
        return top == 0;
    }

    public void push( E elem ) {
        elems[top] = elem;
        top++;
    }
    public E pop() {
        E temp;
        temp = elems[top-1];
        elems[top-1] = null;
        top--;
        return temp;
    }

    public E peek() {
        return elems[top-1];
    }

    public int size() {
        return top;
    }
}
