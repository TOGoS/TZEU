package togos.tzeu;

public interface ImmutableList
{
	public Object get(int idx);
	public ImmutableList with(int idx, Object item);
}
