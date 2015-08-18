using UnityEngine;
using System.Collections;
using System;

[RequireComponent(typeof(Camera))]
public class CastCamera : MonoBehaviour
{

	public GameObject debugObject;

	private Camera _camera;
	private Display _castDisplay;
	private bool _isResolutionSet;
	private int _displayIndex = 1;//hardcoded, if at some point casting to multiple displays is supported, change this.

	private void Start ( )
	{
		_camera = GetComponent<Camera>( );
		_camera.enabled = false;
		_isResolutionSet = false;
	}

	void Update ( )
	{

		if ( Display.displays.Length >= _displayIndex && _isResolutionSet && !_camera.enabled )
		{
			_castDisplay = Display.displays[_displayIndex];
			_camera.SetTargetBuffers( _castDisplay.colorBuffer, _castDisplay.depthBuffer );
			_camera.enabled = true;

			if ( debugObject != null )
				debugObject.GetComponent<Renderer>( ).material.color = Color.red;
		}
		if ( Display.displays.Length < _displayIndex )
		{
			_camera.enabled = false;
		}
	}

	//this gets called from android native code
	public void HandleNativeMessage ( string message )
	{

		string[] parameter = message.Split( ',' );

		if ( parameter.Length > 0 )
		{
			if ( parameter[0].Equals( "SetResolution" ) && parameter.Length == 3 )
			{
				SetResolution( int.Parse( parameter[1] ), int.Parse( parameter[2] ), int.Parse( parameter[3] ) );
			}
		}

	}

	private void SetResolution (int displayIndex, int width, int height )
	{
		if ( displayIndex == _displayIndex && _displayIndex < Display.displays.Length )
		{
			Display.displays[_displayIndex].SetRenderingResolution( width, height );

			if ( debugObject != null )
				debugObject.GetComponent<Renderer>( ).material.color = Color.green;
		}

		_isResolutionSet = true;
	}

}
