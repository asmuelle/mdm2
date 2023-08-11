import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PeerDetailComponent } from './peer-detail.component';

describe('Peer Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PeerDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PeerDetailComponent,
              resolve: { peer: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(PeerDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load peer on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PeerDetailComponent);

      // THEN
      expect(instance.peer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
