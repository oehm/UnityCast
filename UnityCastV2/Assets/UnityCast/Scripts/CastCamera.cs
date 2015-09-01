using UnityEngine;
using System.Collections;
using System;

[RequireComponent( typeof( Camera ) )]
public class CastCamera : MonoBehaviour
{
	public readonly int displayIndex = 1;//hardcoded, if at some point casting to multiple displays is supported, change this.

	private Camera _camera;
	private Display _castDisplay;
	private bool _isResolutionSet;

	private void OnEnable ( )
	{
		CastMessageHandler.AddCastCamera( this );

		_camera = GetComponent<Camera>( );
		_camera.enabled = false;
		_castDisplay = null;
		_isResolutionSet = false;
	}

	private void OnDisable ( )
	{
		CastMessageHandler.RemoveCastCamera( this );
		_camera.enabled = false;
	}

	private void Update ( )
	{
		if ( Display.displays.Length > displayIndex 
			&& _isResolutionSet && !_camera.enabled )
		{
			//set up and enable the camera when the display becomes available

			_castDisplay = Display.displays[displayIndex];
			_camera.SetTargetBuffers( _castDisplay.colorBuffer, _castDisplay.depthBuffer );
			_camera.enabled = true;
		}

		if ( Display.displays.Length < displayIndex )
		{
			//disable the camera when the display becomes unavailable

			_castDisplay = null;
			_camera.enabled = false;
			_isResolutionSet = false;
		}
	}

	//sets the render resolution. called indirectly from android native code, to fit to the target screen.
	public void SetResolution ( int width, int height )
	{
		if ( displayIndex < Display.displays.Length )
		{
			//set the render resolution
			Display.displays[displayIndex].SetRenderingResolution( width, height );
			_isResolutionSet = true;

			if ( _castDisplay != null )
			{
				//if a cast display is already in use, let the camera reset.
				_camera.enabled = false;
			}
		}
		else
		{
			Debug.LogError( name + ": Tried to set the resolution of display " + displayIndex + ", which is not available." );
		}
	}

}
