using UnityEngine;
using System.Collections.Generic;

public class CastMessageHandler : MonoBehaviour //this should be a singleton in the future
{
	private static List<CastCamera> __castCameras = new List<CastCamera>();

	private void Start()
	{
		name = "Cast Message Handler"; //ensure this. otherwise native calls dont reach this object.
	}

	public static void AddCastCamera ( CastCamera castCamera )
	{
		if ( !__castCameras.Contains( castCamera ) )
		{
			__castCameras.Add( castCamera );
		}
	}

	public static void RemoveCastCamera ( CastCamera castCamera )
	{
		if ( __castCameras.Contains( castCamera ) )
		{
			__castCameras.Remove( castCamera );
		}
	}

	//handles incoming messages from android native code. should not be used otherwise.
	public void HandleNativeMessage ( string message )
	{
		string[] parameter = message.Split( ',' );

		if ( parameter.Length > 0 )
		{
			if ( parameter[0].Equals( "SetResolution" ) && parameter.Length == 4)
			{
				//set the resolution on all CastCameras with the right display index

				int displayIndex = int.Parse( parameter[1] );

				foreach ( CastCamera castCamera in __castCameras )
				{
					if ( castCamera.displayIndex == displayIndex )
					{
						castCamera.SetResolution( int.Parse( parameter[2] ), int.Parse( parameter[3] ) );
					}
				}

			}

			//handle future other messages here
		}
	}
}
