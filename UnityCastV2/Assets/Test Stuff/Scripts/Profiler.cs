using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class Profiler : MonoBehaviour
{
	public Text renderedFrameCountText;
	public Text realtimeText;

	private long _renderedFrameCount;

	private void OnEnable ( )
	{
		_renderedFrameCount = 0;
	}

	private void Update ( )
	{
		_renderedFrameCount++;
		if ( renderedFrameCountText != null)
			renderedFrameCountText.text = "" + _renderedFrameCount;

		if ( realtimeText != null )
			realtimeText.text = "" + Time.realtimeSinceStartup;
	}
}
