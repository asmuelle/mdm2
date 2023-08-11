import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../peer.test-samples';

import { PeerFormService } from './peer-form.service';

describe('Peer Form Service', () => {
  let service: PeerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PeerFormService);
  });

  describe('Service methods', () => {
    describe('createPeerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPeerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            utility: expect.any(Object),
            loadType: expect.any(Object),
            owner: expect.any(Object),
          })
        );
      });

      it('passing IPeer should create a new form with FormGroup', () => {
        const formGroup = service.createPeerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            utility: expect.any(Object),
            loadType: expect.any(Object),
            owner: expect.any(Object),
          })
        );
      });
    });

    describe('getPeer', () => {
      it('should return NewPeer for default Peer initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPeerFormGroup(sampleWithNewData);

        const peer = service.getPeer(formGroup) as any;

        expect(peer).toMatchObject(sampleWithNewData);
      });

      it('should return NewPeer for empty Peer initial value', () => {
        const formGroup = service.createPeerFormGroup();

        const peer = service.getPeer(formGroup) as any;

        expect(peer).toMatchObject({});
      });

      it('should return IPeer', () => {
        const formGroup = service.createPeerFormGroup(sampleWithRequiredData);

        const peer = service.getPeer(formGroup) as any;

        expect(peer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPeer should not enable id FormControl', () => {
        const formGroup = service.createPeerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPeer should disable id FormControl', () => {
        const formGroup = service.createPeerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
