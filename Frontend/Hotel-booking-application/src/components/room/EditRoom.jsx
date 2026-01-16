import React, { useState, useEffect } from "react"
import { getRoomById, updateRoom } from "../utils/ApiFunctions"
import { useParams, Link } from "react-router-dom"

const EditRoom = () => {
    const [room, setRoom] = useState({
        photo: null,
        roomType: "",
        roomPrice: "",
        status: ""
    })

    const [imagePreview, setImagePreview] = useState("")
    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")
    const { roomId } = useParams()

    useEffect(() => {
        const fetchRoom = async () => {
            try {
                const response = await getRoomById(roomId)
                setRoom(response.data)
                setImagePreview(`data:image/png;base64,${response.data.photo}`)
            } catch (error) {
                console.error(error)
            }
        }
        fetchRoom()
    }, [roomId])

    const handleInputChange = (event) => {
        const { name, value } = event.target
        setRoom({ ...room, [name]: value })
    }

    const handleImageChange = (e) => {
        const selectedImage = e.target.files[0]
        setRoom({ ...room, photo: selectedImage })
        setImagePreview(URL.createObjectURL(selectedImage))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        try {
            const response = await updateRoom(roomId, room.roomType, room.roomPrice, room.photo, room.status)
            if (response.status === 200) {
                setSuccessMessage("Room updated successfully!")
                const updatedRoomData = await getRoomById(roomId)
                setRoom(updatedRoomData.data)
                setErrorMessage("")
            } else {
                setErrorMessage("Error updating room")
            }
        } catch (error) {
            setErrorMessage(error.message)
        }
    }

    return (
        <div className="container mt-5 mb-5">
            <h3 className="text-center mb-5">Edit Room</h3>
            <div className="row justify-content-center">
                <div className="col-md-8 col-lg-6">
                    {successMessage && <div className="alert alert-success">{successMessage}</div>}
                    {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="roomType" className="form-label">Room Type</label>
                            <input className="form-control" id="roomType" name="roomType" value={room.roomType} onChange={handleInputChange} />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="roomPrice" className="form-label">Room Price</label>
                            <input className="form-control" id="roomPrice" name="roomPrice" type="number" value={room.roomPrice} onChange={handleInputChange} />
                        </div>
                        
                        {/* Status Toggle Dropdown */}
                        <div className="mb-3">
                            <label htmlFor="status" className="form-label">Room Status</label>
                            <select className="form-select" id="status" name="status" value={room.status} onChange={handleInputChange}>
                                <option value="AVAILABLE">AVAILABLE</option>
                                <option value="MAINTENANCE">MAINTENANCE</option>
                            </select>
                        </div>

                        <div className="mb-3">
                            <label htmlFor="photo" className="form-label">Room Photo</label>
                            <input type="file" className="form-control" id="photo" name="photo" onChange={handleImageChange} />
                            {imagePreview && (
                                <img src={imagePreview} alt="Room preview" style={{ maxWidth: "400px", maxHeight: "400px" }} className="mt-3" />
                            )}
                        </div>

                        <div className="d-grid gap-2 d-md-flex mt-2">
                            <Link to={"/existing-rooms"} className="btn btn-outline-info">Back</Link>
                            {/* Updated button text to "Update Room" to avoid confusion */}
                            <button type="submit" className="btn btn-outline-warning">Update Room</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    )
}
export default EditRoom