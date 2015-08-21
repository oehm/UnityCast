using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class Profiler : MonoBehaviour
{
	public Text[] frameTexts;
	public Text[] timeTexts;
	public Image blinker;

	private int _textIndex;

	private long _frameCount;

	private bool _isSetUpCorrectly;

	private void OnEnable ( )
	{
		_frameCount = 0;

		_textIndex = 0;

		_isSetUpCorrectly = frameTexts.Length >= 2 && timeTexts.Length == frameTexts.Length && blinker != null;

		if ( !_isSetUpCorrectly )
		{
			enabled = false;
		}
	}

	private void Update ( )
	{
		if ( !_isSetUpCorrectly )
		{
			return;
		}

		_frameCount++;

		int lastTextIndex = _textIndex;
		_textIndex = ( _textIndex + 1 ) % frameTexts.Length;
		int nextTextIndex = ( _textIndex + 1 ) % frameTexts.Length;

		
		frameTexts[lastTextIndex].color = Color.black;

		frameTexts[_textIndex].color = Color.red;
		frameTexts[_textIndex].text = _frameCount.ToString( "000000" );
		;

		frameTexts[nextTextIndex].text = "";


		timeTexts[lastTextIndex].color = Color.black;

		timeTexts[_textIndex].color = Color.red;
		timeTexts[_textIndex].text = Time.realtimeSinceStartup.ToString( "00.000" );
		;

		timeTexts[nextTextIndex].text = "";

		blinker.enabled = (int) ( Time.realtimeSinceStartup ) % 2 == 0;

	}



}
